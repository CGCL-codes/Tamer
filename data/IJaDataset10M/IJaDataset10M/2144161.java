package org.tgworks.commons.regex.impl.jakarta.regex;

import org.apache.regexp.RE;
import org.tgworks.commons.regex.Pattern;
import org.tgworks.commons.regex.impl.base.AbstractMatcher;

class JakartaMatcher extends AbstractMatcher {

    private RE re;

    JakartaMatcher(JakartaPattern pattern, CharSequence input) {
        super(pattern, input);
        this.re = pattern.getRE();
    }

    @Override
    protected String doReplace(String replacement, boolean replaceAll) {
        int option = RE.REPLACE_BACKREFERENCES;
        if (!replaceAll) {
            option |= RE.REPLACE_FIRSTONLY;
        }
        return this.re.subst(this.input(), replacement, option);
    }

    @Override
    protected void doReset() {
    }

    @Override
    protected void doUsePattern(Pattern newPattern) {
        this.re = ((JakartaPattern) newPattern).getRE();
        this.reset();
    }

    public int end(int group) {
        return this.re.getParenEnd(group);
    }

    public String group(int group) {
        return this.re.getParen(group);
    }

    public int groupCount() {
        return this.re.getParenCount() - 1;
    }

    @Override
    protected boolean search() {
        boolean result = this.re.match(this.input(), this.getNextStartPosition());
        return result;
    }

    public int start(int group) {
        return this.re.getParenStart(group);
    }
}
