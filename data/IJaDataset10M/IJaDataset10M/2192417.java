package de.meylirh.pwkit.component.charManager;

import java.util.Date;
import de.meylirh.pwkit.domain.CDKey;

public interface NWNCDKey {

    int getID();

    CDKey getKey();

    Date getCreated();
}
