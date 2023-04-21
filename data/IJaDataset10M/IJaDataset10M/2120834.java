package com.serena.xmlbridge.adapter.p4;

import com.serena.xmlbridge.adapter.p4.client.Env;
import com.serena.xmlbridge.adapter.p4.client.Job;
import com.serena.xmlbridge.adapter.p4.client.JobField;
import com.serena.xmlbridge.adapter.p4.client.PerforceException;
import com.serena.xmlbridge.adapter.AbstractAdapter;
import com.serena.xmlbridge.adapter.ttwebservice.gen.ItemData;
import com.serena.xmlbridge.adapter.ttwebservice.gen.ItemDataList;
import com.serena.xmlbridge.adapter.ttwebservice.gen.NameValue;
import com.serena.xmlbridge.adapter.ttwebservice.gen.ObjectFactory;
import com.serena.xmlbridge.entities.exceptions.InvalidItemDataException;
import com.serena.xmlbridge.entities.exceptions.XBridgeNGException;
import com.serena.xmlbridge.util.DateParser;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Brian Rosenberger
 */
public class P4Adapter extends AbstractAdapter {

    /** Creates a new instance of P4Adapter */
    public P4Adapter() {
        this.env = new Env();
    }

    public void setProperties(HashMap<String, String> props) {
        for (Iterator<String> iter = props.keySet().iterator(); iter.hasNext(); ) {
            String key = iter.next();
            String value = props.get(key);
            this.env.setProperty(key, value);
        }
    }

    private Env env;

    public void setClient(String client) {
        this.env.setClient(client);
        client = null;
    }

    public void setExecutable(File exe) {
        this.env.setExecutable(exe.toString());
        exe = null;
    }

    public void setPassword(String pwd) {
        this.env.setPassword(pwd);
        pwd = null;
    }

    public void setUser(String usr) {
        this.env.setUser(usr);
        usr = null;
    }

    public void setPort(String port) {
        this.env.setPort(port);
    }

    public void setPropertyfile(String file) {
        try {
            this.env.setFromProperties(file);
        } catch (PerforceException ex) {
            getExceptionHandler().putException(new XBridgeNGException("Error loading P4 properties", ex));
        }
    }

    public void setTimeout(long millis) {
        this.env.setServerTimeout(millis);
    }

    public void setSystemdrive(String drive) {
        this.env.setSystemDrive(drive);
    }

    public void setSystemroot(String file) {
        this.env.setSystemRoot(file);
    }

    public ItemData updateP4Job(ItemData item) {
        Job job = null;
        try {
            this.env.checkValidity();
            boolean isnew = false;
            String myid = null;
            if (!item.getGenericItem().getItemName().equals(this.getName())) {
                myid = getXrefHandler().getMyId(item.getGenericItem().getItemName(), item.getGenericItem().getItemID());
            } else {
                myid = item.getGenericItem().getItemID();
            }
            if (myid == null) {
                myid = "new";
                isnew = true;
                System.out.println("Creating new job.");
            } else {
                System.out.println("Update to job '" + myid + "'.");
            }
            job = Job.getJob(this.env, myid);
            JobField jfld = new JobField();
            jfld.loadFields(this.env);
            Iterator<NameValue> iter = item.getExtendedFieldList().iterator();
            while (iter.hasNext()) {
                NameValue nv = iter.next();
                if (nv.getName() != null && nv.getName().length() > 0) {
                    String value = "";
                    if (nv.getValue() != null) {
                        JobField fld = jfld.getField(nv.getName());
                        if (fld == null) {
                            System.out.println("Field '" + nv.getName() + "' does not exist in P4");
                            continue;
                        }
                        int datatype = fld.getDataType();
                        switch(datatype) {
                            case JobField.DATE:
                                SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                value = fmt.format(DateParser.parse(nv.getValue().getValue()));
                                break;
                            default:
                                value = nv.getValue().getValue().replaceAll("\n", "\n ");
                        }
                        job.setField(nv.getName(), value);
                    } else {
                        System.out.println("Multi-Value field with name '" + nv.getName() + "' not yet supported.");
                    }
                }
            }
            job.commit();
            if (isnew) {
                getXrefHandler().setXref(item.getGenericItem().getItemName(), item.getGenericItem().getItemID(), job.getName());
            }
            item = toItemData(job);
        } catch (Exception e) {
            this.getExceptionHandler().putException(new InvalidItemDataException("Error creating P4Job.", e, item));
        }
        return item;
    }

    private ItemData toItemData(Job job) {
        job.setEnv(this.env);
        job.sync();
        ObjectFactory of = new ObjectFactory();
        ItemData item = of.createItemData();
        item.setGenericItem(of.createItem());
        item.getGenericItem().setItemID(job.getName());
        item.getGenericItem().setItemName(getName());
        Enumeration<String> en = job.getFieldNames();
        while (en.hasMoreElements()) {
            String key = en.nextElement();
            String value = job.getField(key);
            NameValue nv = of.createNameValue();
            nv.setName(key);
            nv.setValue(of.createNameValueValue(value));
            item.getExtendedFieldList().add(nv);
            nv = null;
        }
        job = null;
        return item;
    }

    private String jobview = "*";

    public void setJobview(String jobview) {
        this.jobview = jobview;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    private int limit = 1000;

    public ItemDataList getJobsByJobview() {
        ObjectFactory of = new ObjectFactory();
        ItemDataList itemlist = of.createItemDataList();
        try {
            this.env.checkValidity();
            Job[] jobs = Job.getJobs(this.env, jobview, limit, false, null);
            for (int i = 0; i < jobs.length; i++) {
                itemlist.getItemData().add(toItemData(jobs[i]));
            }
        } catch (PerforceException ex) {
            getExceptionHandler().putException(new XBridgeNGException("Invalid configuration.", ex, null));
        }
        return itemlist;
    }
}
