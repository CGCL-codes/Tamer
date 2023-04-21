package com.mapping;

import net.rim.blackberry.api.options.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.i18n.*;
import net.rim.device.api.system.*;
import net.rim.device.api.collection.util.*;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.compress.GZIPInputStream;
import javax.microedition.io.Connector;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class BANK {

    private String _dataFile = "/data/ATMFinder_DATA_BANK.gz";

    public int _numof_ = 0;

    public String _ID_[];

    public String _DETAIL_[];

    public BANK() {
        SetDataOf();
    }

    private String Split_ID_(int i, String _str_) {
        return _str_.substring(i == 0 ? 1 : 0, _str_.indexOf("$"));
    }

    private String Split_DETAIL_(int i, String _str_) {
        String _DETAIL_ = _str_.substring(_str_.indexOf("$") + 1, _str_.length());
        return _DETAIL_.substring(0, _DETAIL_.indexOf("$"));
    }

    private void SetDataOf() {
        try {
            InputStream inread = getClass().getResourceAsStream(_dataFile);
            GZIPInputStream izip = new GZIPInputStream(inread);
            InputStreamReader isr = new InputStreamReader(izip, "utf-8");
            _numof_ = 0;
            String[] _ID_temp = new String[40];
            String[] _DETAIL_temp = new String[40];
            String _data_temp = "";
            char[] _byte_load = new char[100];
            while (isr.read(_byte_load, 0, 100) > 0) {
                _data_temp = String.valueOf(_byte_load);
                _ID_temp[_numof_] = Split_ID_(_numof_, _data_temp);
                _DETAIL_temp[_numof_] = Split_DETAIL_(_numof_, _data_temp);
                _numof_++;
            }
            _ID_ = new String[_numof_];
            _DETAIL_ = new String[_numof_];
            for (int i = 0; i < _numof_; i++) {
                _ID_[i] = _ID_temp[i];
                _DETAIL_[i] = _DETAIL_temp[i];
            }
            isr.close();
            izip.close();
            inread.close();
        } catch (java.io.IOException ex) {
            Dialog.inform(ex.toString());
        }
    }
}
