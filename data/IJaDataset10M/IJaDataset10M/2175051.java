package org.gudy.azureus2.core3.ipfilter.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.gudy.azureus2.core3.config.COConfigurationManager;
import org.gudy.azureus2.core3.config.ParameterListener;
import org.gudy.azureus2.core3.ipfilter.BadIps;
import org.gudy.azureus2.core3.ipfilter.IpFilter;
import org.gudy.azureus2.core3.ipfilter.IpFilterManager;
import org.gudy.azureus2.core3.ipfilter.IpRange;
import org.gudy.azureus2.core3.util.FileUtil;

public class IpFilterManagerImpl implements IpFilterManager, ParameterListener {

    protected static IpFilterManagerImpl singleton = new IpFilterManagerImpl();

    private RandomAccessFile rafDescriptions = null;

    /**
	 * 
	 */
    public IpFilterManagerImpl() {
        COConfigurationManager.addAndFireParameterListener("Ip Filter Enable Description Cache", this);
    }

    public Object addDescription(IpRange range, byte[] description) {
        if (rafDescriptions == null) {
            return null;
        }
        try {
            if (description == null || description.length == 0) return null;
            int start;
            int end;
            start = (int) rafDescriptions.getFilePointer();
            int len = (int) rafDescriptions.length();
            if (len + 61 >= 0x1FFFFFF) {
                return null;
            }
            if (start != len) {
                rafDescriptions.seek(len);
                start = (int) rafDescriptions.getFilePointer();
            }
            if (description.length <= 61) {
                rafDescriptions.write(description);
            } else {
                rafDescriptions.write(description, 0, 61);
            }
            end = (int) rafDescriptions.getFilePointer();
            int info = start + ((end - start) << 25);
            return new Integer(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] getDescription(Object info) {
        if (info instanceof Object[]) {
            return (byte[]) (((Object[]) info)[0]);
        }
        if (rafDescriptions == null || !(info instanceof Integer)) {
            return "".getBytes();
        }
        try {
            int posInfo = ((Integer) info).intValue();
            int pos = posInfo & 0x1FFFFFF;
            int len = posInfo >> 25;
            if (len < 0) {
                throw new IllegalArgumentException(getClass().getName() + ": invalid posInfo [" + posInfo + "], pos [" + pos + "], len [" + len + "]");
            }
            if (rafDescriptions.getFilePointer() != pos) {
                rafDescriptions.seek(pos);
            }
            byte[] bytes = new byte[len];
            rafDescriptions.read(bytes);
            return bytes;
        } catch (IOException e) {
            return "".getBytes();
        }
    }

    public void cacheAllDescriptions() {
        IpRange[] ranges = getIPFilter().getRanges();
        for (int i = 0; i < ranges.length; i++) {
            Object info = ((IpRangeImpl) ranges[i]).getDescRef();
            if (info instanceof Integer) {
                byte[] desc = getDescription(info);
                Object[] data = { desc, info };
                ((IpRangeImpl) ranges[i]).setDescRef(data);
            }
        }
    }

    public void clearDescriptionCache() {
        IpRange[] ranges = getIPFilter().getRanges();
        for (int i = 0; i < ranges.length; i++) {
            Object info = ((IpRangeImpl) ranges[i]).getDescRef();
            if (info instanceof Object[]) {
                Integer data = (Integer) ((Object[]) info)[1];
                ((IpRangeImpl) ranges[i]).setDescRef(data);
            }
        }
    }

    public void deleteAllDescriptions() {
        if (rafDescriptions != null) {
            try {
                rafDescriptions.close();
            } catch (IOException e) {
            }
            rafDescriptions = null;
        }
        parameterChanged(null);
    }

    public static IpFilterManager getSingleton() {
        return (singleton);
    }

    public IpFilter getIPFilter() {
        return (IpFilterImpl.getInstance());
    }

    public BadIps getBadIps() {
        return (BadIpsImpl.getInstance());
    }

    public void parameterChanged(String parameterName) {
        boolean enable = COConfigurationManager.getBooleanParameter("Ip Filter Enable Description Cache");
        if (enable && rafDescriptions == null) {
            File fDescriptions = FileUtil.getUserFile("ipfilter.cache");
            try {
                if (fDescriptions.exists()) {
                    fDescriptions.delete();
                }
                rafDescriptions = new RandomAccessFile(fDescriptions, "rw");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (!enable && rafDescriptions != null) {
            try {
                rafDescriptions.close();
            } catch (IOException e) {
            }
            rafDescriptions = null;
        }
    }
}
