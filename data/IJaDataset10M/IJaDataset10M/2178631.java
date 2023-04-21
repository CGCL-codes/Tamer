package org.unicef.doc.ibis.nut.controllers;

import org.unicef.doc.ibis.nut.exceptions.*;
import org.unicef.doc.ibis.nut.persistence.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author ngroupp
 */
public class MetaDataController extends MutableController {

    MetaData fMetaData;

    public MetaDataController(MetaData pMetaData) throws CreationTimeException, ModificationTimeException, NUTException, IOException, javax.xml.datatype.DatatypeConfigurationException {
        super(pMetaData);
        fMetaData = pMetaData;
        buildTree();
    }

    @Override
    public void buildTree() throws CreationTimeException, ModificationTimeException, LockException, NUTException, IOException {
        super.buildTree();
        if (fMetaData.getEntry() == null) {
            lock();
            fMetaData.setEntry(new LinkedList<MetaDatum>());
            unlock();
        }
    }

    public void addMetaDatum(MetaDatum pMetaDatum) throws ModificationTimeException, LockException, NUTException, IOException {
        lock();
        fMetaData.getEntry().add(pMetaDatum);
        unlock();
    }
}
