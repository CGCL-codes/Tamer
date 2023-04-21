package org.equanda.test.om.server;

import org.equanda.persistence.om.EquandaPersistenceException;

/**
 * Provisions for testing the order of setting fields in setEquandaProxy calls
 *
 * @author NetRom team
 */
public class PriorityFieldsMediator extends PriorityFieldsMediatorBase {

    private void add(char number) {
        String start = entity.getTest();
        if (start == null) start = "";
        entity.setTest(start + number);
    }

    public void setField0(boolean field0) throws EquandaPersistenceException {
        add('0');
        super.setField0(field0);
    }

    public void setField1(boolean field0) throws EquandaPersistenceException {
        add('1');
        super.setField0(field0);
    }

    public void setField2(boolean field0) throws EquandaPersistenceException {
        add('2');
        super.setField0(field0);
    }

    public void setField3(boolean field0) throws EquandaPersistenceException {
        add('3');
        super.setField0(field0);
    }

    public void setField4(boolean field0) throws EquandaPersistenceException {
        add('4');
        super.setField0(field0);
    }

    public void setField5(boolean field0) throws EquandaPersistenceException {
        add('5');
        super.setField0(field0);
    }

    public void setField6(boolean field0) throws EquandaPersistenceException {
        add('6');
        super.setField0(field0);
    }

    public void setField7(boolean field0) throws EquandaPersistenceException {
        add('7');
        super.setField0(field0);
    }

    public void setField8(boolean field0) throws EquandaPersistenceException {
        add('8');
        super.setField0(field0);
    }

    public void setField9(boolean field0) throws EquandaPersistenceException {
        add('9');
        super.setField0(field0);
    }
}
