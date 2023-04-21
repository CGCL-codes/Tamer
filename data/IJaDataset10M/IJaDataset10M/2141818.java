package com.ericdaugherty.mail.server.configuration.cbc;

import com.ericdaugherty.mail.server.configuration.ConnectionBasedConfiguratorConstants;
import java.util.ListIterator;

/**
 *
 * @author Andreas Kyrmegalos
 */
public abstract class CBCResponseExecutor implements ConnectionBasedConfiguratorConstants {

    protected ListIterator<String> iter;

    public CBCResponseExecutor(ListIterator<String> iter) {
        this.iter = iter;
    }

    public abstract byte[] processLines();
}
