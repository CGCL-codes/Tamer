package com.avatal.content.vo.course.scorm.scormMetaData;

import com.avatal.business.util.AvArrayList;
import com.avatal.vo.base.AbstractIdentifyingNumberObject;

public class General extends AbstractIdentifyingNumberObject {

    public String identifier;

    public String title;

    /**
     *@link aggregation
     *      @associates <{com.avatal.content.vo.course.scorm.scormMetaData.CatalogEntry}>
     */
    public AvArrayList catalogentrys;

    public AvArrayList languages;

    public AvArrayList descriptions;

    public AvArrayList keywords;

    public General() {
        catalogentrys = new AvArrayList();
        languages = new AvArrayList();
        descriptions = new AvArrayList();
        keywords = new AvArrayList();
    }
}
