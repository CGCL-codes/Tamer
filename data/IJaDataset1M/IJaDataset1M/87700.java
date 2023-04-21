package gxbind.extjs.ext.form.config;

import gxbind.extjs.ext.form.Validator;

public interface ITextFieldWritable {

    void setAllowBlank(final boolean isAllowBlank);

    void setBlankText(final String blankText);

    void setDisableKeyFilter(final boolean isDisableKeyFilter);

    void setEmptyClass(final String emptyClass);

    void setEmptyText(final String emptyText);

    void setGrow(final boolean isGrow);

    void setGrowMax(final int growMax);

    void setGrowMin(final int growMin);

    void setMaskRe(final String maskRe);

    void setMaxLength(final int maxLength);

    void setMaxLengthText(final String maxLengthText);

    void setMinLength(final int minLength);

    void setMinLengthText(final String minLengthText);

    void setRegex(final String regExp);

    void setRegexText(final String regexText);

    void setSelectOnFocus(final boolean isSelectOnFocus);

    void setValidator(final Validator validator);

    void setVtype(final String vtype);
}
