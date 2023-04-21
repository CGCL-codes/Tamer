package com.avatal.content.vo.course.scorm;

import com.avatal.content.business.ejb.entity.scormcourse.ResourceEntityData;

public class ScormCourseSCOVo extends ResourceEntityData {

    public String description, keywords, language, title, masteryScore;

    /**
 * occurs 0 ore 1 time
 */
    public MetaData MetaData;

    /**
 * The organizations Element occurs 1 and only 1 time in the manifest
 * holds 0 ore more Organization Objects
 * 
 */
    public ScormCourseSCOVo(ResourceEntityData data) {
        super(data);
    }

    public ScormCourseSCOVo(ResourceEntityData data, String masteryScore) {
        super(data);
        this.masteryScore = masteryScore;
    }

    public ScormCourseSCOVo(ResourceEntityData data, String description, String keywords, String language, String title) {
        super(data);
        this.description = description;
        this.title = title;
        this.keywords = keywords;
        this.language = language;
    }
}
