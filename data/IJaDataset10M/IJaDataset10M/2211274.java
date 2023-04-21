package org.gzigzag.modules.mm;

public class MediaZOb {

    public final org.gzigzag.Stepper x;

    public final org.gzigzag.Stepper y;

    public MediaZOb(org.gzigzag.Stepper s) {
        x = (org.gzigzag.Stepper) (org.gzigzag.impl.ZObFactory.getFactory().findMember(s, "0000000008000000EC479602E300048C67285A5B7AD5240E65EB2E1B867BB1BDABFA6827D8D99F-5"));
        y = (org.gzigzag.Stepper) (org.gzigzag.impl.ZObFactory.getFactory().findMember(s, "0000000008000000EC479602E300048C67285A5B7AD5240E65EB2E1B867BB1BDABFA6827D8D99F-6"));
    }
}
