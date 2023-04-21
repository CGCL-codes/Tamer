package com.vlee.ejb.ecommerce;

import java.sql.*;
import javax.sql.*;
import java.util.*;
import java.math.*;
import java.io.Serializable;
import com.vlee.util.*;

public class EStaticPageObject implements Serializable {

    public Long pkid;

    public String page_title;

    public String page_content;

    public boolean inc_header;

    public boolean inc_leftbar;

    public boolean inc_footer;

    public Timestamp dateCreated;

    public Timestamp dateLastEdit;

    public Integer useridCreate;

    public Integer useridEdit;

    public String page_type;

    public EStaticPageObject() {
        this.pkid = new Long(0);
        this.page_title = "";
        this.page_content = "";
        this.inc_header = true;
        this.inc_leftbar = true;
        this.inc_footer = true;
        this.dateCreated = TimeFormat.getTimestamp();
        this.dateLastEdit = TimeFormat.getTimestamp();
        this.useridCreate = new Integer(0);
        this.useridEdit = new Integer(0);
        this.page_type = EStaticPageBean.PAGE_TYPE_HTML;
    }
}
