package com.frameworkset.common.tag.pager.tags;

import java.io.OutputStream;
import java.io.Serializable;

public final class LastTag extends JumpTagSupport implements Serializable {

    protected long getJumpPage() {
        return (pagerContext.getPageCount() - 1);
    }

    public String generateContent() {
        return null;
    }

    public void write(OutputStream output) {
    }
}
