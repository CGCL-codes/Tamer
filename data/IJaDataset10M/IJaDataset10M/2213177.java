package ru.adv.db.adapter;

import ru.adv.db.config.Trigger;
import ru.adv.db.config.PersistentEvent;
import ru.adv.db.config.VariableInfo;
import ru.adv.db.config.ConfigObject;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.List;

/**
 * User: roma
 * Date: Apr 11, 2003
 * Time: 7:12:23 PM
 * $Id: OracleTriggerSQLGenerator.java 1106 2009-06-03 07:32:17Z vic $
 * $Name:  $
 */
public class OracleTriggerSQLGenerator implements TriggerSQLGenerator {

    private List _triggers[];

    private static final String _prefixes[] = { "bir", "bur", "bdr", "air", "aur", "adr", "iir", "iur", "idr", "bis", "bus", "bds", "ais", "aus", "ads", "iis", "ius", "ids" };

    private static final String _events[] = { "BEFORE INSERT", "BEFORE UPDATE", "BEFORE DELETE", "AFTER INSERT", "AFTER UPDATE", "AFTER DELETE", "INSTEAD OF INSERT", "INSTEAD OF UPDATE", "INSTEAD OF DELETE", "BEFORE INSERT", "BEFORE UPDATE", "BEFORE DELETE", "AFTER INSERT", "AFTER UPDATE", "AFTER DELETE", "INSTEAD OF INSERT", "INSTEAD OF UPDATE", "INSTEAD OF DELETE" };

    private static final int BIR = 0;

    private static final int BUR = 1;

    private static final int BDR = 2;

    private static final int AIR = 3;

    private static final int AUR = 4;

    private static final int ADR = 5;

    private static final int IIR = 6;

    private static final int IUR = 7;

    private static final int IDR = 8;

    private static final int BIS = 9;

    private static final int BUS = 10;

    private static final int BDS = 11;

    private static final int AIS = 12;

    private static final int AUS = 13;

    private static final int ADS = 14;

    private static final int IIS = 15;

    private static final int IUS = 16;

    private static final int IDS = 17;

    private List<String> _sqls;

    private Oracle _adapter;

    OracleTriggerSQLGenerator(Oracle adapter) {
        _adapter = adapter;
        _sqls = new LinkedList<String>();
        _triggers = new LinkedList[_prefixes.length];
        for (int i = 0; i < _prefixes.length; ++i) {
            _triggers[i] = new LinkedList();
        }
    }

    public void add(Trigger trigger) throws DBAdapterException {
        if (trigger == null) {
            return;
        }
        if (trigger.getEvents().contains(PersistentEvent.BEFORE_INSERT)) {
            if (trigger.isForEachRow()) {
                _triggers[BIR].add(trigger);
            } else {
                _triggers[BIS].add(trigger);
            }
        }
        if (trigger.getEvents().contains(PersistentEvent.AFTER_INSERT)) {
            if (trigger.isForEachRow()) {
                _triggers[AIR].add(trigger);
            } else {
                _triggers[AIS].add(trigger);
            }
        }
        if (trigger.getEvents().contains(PersistentEvent.INSTEAD_OF_INSERT)) {
            if (trigger.isForEachRow()) {
                _triggers[IIR].add(trigger);
            } else {
                _triggers[IIS].add(trigger);
            }
        }
        if (trigger.getEvents().contains(PersistentEvent.BEFORE_UPDATE)) {
            if (trigger.isForEachRow()) {
                _triggers[BUR].add(trigger);
            } else {
                _triggers[BUS].add(trigger);
            }
        }
        if (trigger.getEvents().contains(PersistentEvent.AFTER_UPDATE)) {
            if (trigger.isForEachRow()) {
                _triggers[AUR].add(trigger);
            } else {
                _triggers[AUS].add(trigger);
            }
        }
        if (trigger.getEvents().contains(PersistentEvent.INSTEAD_OF_UPDATE)) {
            if (trigger.isForEachRow()) {
                _triggers[IUR].add(trigger);
            } else {
                _triggers[IUS].add(trigger);
            }
        }
        if (trigger.getEvents().contains(PersistentEvent.BEFORE_DELETE)) {
            if (trigger.isForEachRow()) {
                _triggers[BDR].add(trigger);
            } else {
                _triggers[BDS].add(trigger);
            }
        }
        if (trigger.getEvents().contains(PersistentEvent.AFTER_DELETE)) {
            if (trigger.isForEachRow()) {
                _triggers[ADR].add(trigger);
            } else {
                _triggers[ADS].add(trigger);
            }
        }
        if (trigger.getEvents().contains(PersistentEvent.INSTEAD_OF_DELETE)) {
            if (trigger.isForEachRow()) {
                _triggers[IDR].add(trigger);
            } else {
                _triggers[IDS].add(trigger);
            }
        }
    }

    public void addSQL(String sql) {
        _sqls.add(sql);
    }

    public List<String> getSQL(ConfigObject owner) {
        List<String> sqls = new LinkedList<String>();
        for (int i = 0; i < _triggers.length; i++) {
            List list = _triggers[i];
            String declare = "";
            String body = "";
            for (Iterator itor = list.iterator(); itor.hasNext(); ) {
                Trigger trigger = (Trigger) itor.next();
                for (Iterator vitor = trigger.getVariables().iterator(); vitor.hasNext(); ) {
                    VariableInfo info = (VariableInfo) vitor.next();
                    declare += "    " + info.getName() + " " + info.getOptions() + ";\n";
                }
                body += "    -- start of " + trigger.getId() + "\n\n";
                body += trigger.getBody();
                body += "\n    -- end of " + trigger.getId() + "\n\n";
            }
            if (body.length() > 0) {
                String name = "mz_" + _prefixes[i] + "_" + owner;
                String sql = "CREATE OR REPLACE TRIGGER " + qi(name) + " " + _events[i] + " ON " + owner.getSQLName();
                if (i < BIS) {
                    sql += " FOR EACH ROW";
                }
                sql += "\n";
                if (declare.length() > 0) {
                    sql += "DECLARE\n";
                    sql += declare;
                }
                sql += "BEGIN\n";
                sql += body;
                sql += "END " + qi(name) + ";\n";
                sqls.add(sql);
                sql = "INSERT INTO " + _adapter.getTriggerTableName() + " (tablename,trigname) VALUES ('" + _adapter.registeredName(owner.getName()) + "','" + _adapter.registeredName(name) + "')";
                sqls.add(sql);
            }
        }
        sqls.addAll(_sqls);
        return sqls;
    }

    private String qi(String name) {
        return _adapter.quotedIdentifier(name);
    }
}
