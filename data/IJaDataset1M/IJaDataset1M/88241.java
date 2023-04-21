package com.centraview.common;

/**
 * The MoneyMember holds Float values.
 *
 * @author
 */
public class MoneyMember extends FloatMember {

    public MoneyMember(String memberType, Float memberValue, int auth, String requestURL, char displayType, boolean linkEnabled, int displayWidth) {
        super(memberType, memberValue, auth, requestURL, displayType, linkEnabled, displayWidth);
    }

    /**
   * Returns A String representation of the
   * FloatMember in the following format:
   * ###,###,##0.00 The #'s will not
   * be shown unless there is a value there. If
   * A value is present, the number will be shown.
   *
   * @see java.text.DecimalFormat
   *
   * @return A String representation of the MoneyMember.
   */
    public String getDisplayString() {
        return "$ " + super.getDisplayString();
    }
}
