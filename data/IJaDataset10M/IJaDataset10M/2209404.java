package uk.gov.dti.og.fox;

import javax.servlet.http.HttpServletRequest;
import uk.gov.dti.og.fox.ex.ExServiceUnavailable;
import uk.gov.dti.og.fox.ex.ExInternal;
import uk.gov.dti.og.fox.ex.ExModule;
import uk.gov.dti.og.fox.ex.ExSecurity;
import uk.gov.dti.og.fox.ex.ExUserRequest;

public final class ComponentText extends FoxComponent {

    private final String mName;

    private final String mType;

    private final FoxResponse mFoxResponse;

    private StringBuffer mText;

    public ComponentText(String pName, String pType, StringBuffer pStringBuffer, long pBrowserCacheMilliSec) {
        mName = pName;
        mType = pType;
        mText = pStringBuffer;
        mFoxResponse = new FoxResponseCHAR(pType, pStringBuffer, pBrowserCacheMilliSec);
    }

    public String getName() {
        return mName;
    }

    public FoxResponse processResponse(FoxRequest pRequest, StringBuffer pURLTail) throws ExInternal, ExSecurity, ExUserRequest, ExModule, ExServiceUnavailable {
        return mFoxResponse;
    }

    public final String getType() {
        return mType;
    }

    public final StringBuffer getText() {
        return mText;
    }
}
