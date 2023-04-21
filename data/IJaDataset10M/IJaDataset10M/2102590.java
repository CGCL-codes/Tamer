package com.vlee.ejb.inventory;

import java.sql.*;
import javax.sql.*;
import java.util.*;
import java.math.*;
import java.io.Serializable;
import com.vlee.util.*;

public class InvStringTemplateObject implements Serializable {

    public Long pkid;

    public String category;

    public String code;

    public String sort;

    public String description;

    public String content;

    public String displayTemplate;

    public String displayStyle;

    public String displayFormat;

    public String displayType;

    public InvStringTemplateObject() {
        this.pkid = new Long(0);
        this.category = "";
        this.code = "";
        this.sort = "";
        this.description = "";
        this.content = "";
        this.displayTemplate = "";
        this.displayStyle = "";
        this.displayFormat = "";
        this.displayType = "";
    }
}
