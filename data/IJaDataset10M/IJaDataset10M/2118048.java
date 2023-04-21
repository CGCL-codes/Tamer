package com.fj.engine;

public interface IDescription {

    public static final int NAMETYPE_NORMAL = 0;

    public static final int NAMETYPE_PROPER = 1;

    public static final int NAMETYPE_QUANTITY = 2;

    public static final int GENDER_NEUTER = 0;

    public static final int GENDER_MALE = 1;

    public static final int GENDER_FEMALE = 2;

    public static final int CASE_NOMINATIVE = 0;

    public static final int CASE_ACCUSATIVE = 1;

    public static final int CASE_GENITIVE = 2;

    public static final int ARTICLE_NONE = 0;

    public static final int ARTICLE_DEFINITE = 1;

    public static final int ARTICLE_INDEFINITE = 2;

    public static final int ARTICLE_POSSESIVE = 3;

    public static final int NUMBER_SINGULAR = 1;

    public static final int NUMBER_PLURAL = 1000000;

    public String getName(int number, int article);

    public String getDescriptionText();
}
