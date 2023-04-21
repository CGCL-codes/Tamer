package com.nhncorp.cubridqa.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * A data model is used to describe build server for building schedule test.
 * @ClassName: ChooseBuild
 * @date 2009-9-7
 * @version V1.0
 * Copyright (C) www.nhn.com
 */
@XStreamAlias(value = "chooseBuild")
public class ChooseBuild extends Resource {

    public static final String ALIAS = "choose_build";

    @Override
    public String getTITLE() {
        return "ChooseTestEngine";
    }

    private String testEngine;

    private String buildNumber;

    private String version;

    private String platform;

    private String preFix;

    private String postFix;

    private String extension;

    public ChooseBuild() {
        super();
    }

    public ChooseBuild(String absPath) {
        super(absPath);
    }

    public static ChooseBuild getInstance(String absPath) {
        return (ChooseBuild) Resource.getInstance(absPath);
    }

    public String getTestEngine() {
        return testEngine;
    }

    public void setTestEngine(String testEngine) {
        this.testEngine = testEngine;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPreFix() {
        return preFix;
    }

    public void setPreFix(String preFix) {
        this.preFix = preFix;
    }

    public String getPostFix() {
        return postFix;
    }

    public void setPostFix(String postFix) {
        this.postFix = postFix;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
