package com.ht.use.attack.impl;

import com.ht.use.IUseInvocation;
import com.ht.use.IUseable;

/**
 * A marker interface.
 * 
 * @since 1.0
 */
public interface IAttackInvocation extends IUseInvocation {

    @Override
    public IUseable getItem();
}
