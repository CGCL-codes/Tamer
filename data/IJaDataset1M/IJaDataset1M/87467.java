package com.daffodilwoods.daffodildb.server.datadictionarysystem;

import com.daffodilwoods.daffodildb.server.datadictionarysystem.*;
import com.daffodilwoods.database.resource.*;

public interface _TriggerCharacteristics {

    _Trigger[] getAfterInsertTriggers() throws DException;

    _Trigger[] getAfterUpdateTriggers(int[] columns) throws DException;

    _Trigger[] getAfterDeleteTriggers() throws DException;

    _Trigger[] getBeforeDeleteTriggers() throws DException;

    _Trigger[] getBeforeInsertTriggers() throws DException;

    _Trigger[] getBeforeUpdateTriggers(int[] columns) throws DException;

    void addTrigger(_Trigger trigger) throws DException;

    void removeTrigger(_Trigger trigger) throws DException;
}
