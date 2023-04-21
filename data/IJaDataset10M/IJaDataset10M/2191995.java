package tcl.lang;

import java.util.ArrayList;

class AfterCmd implements Command {

    AfterAssocData assocData = null;

    private static final String validOpts[] = { "cancel", "idle", "info" };

    static final int OPT_CANCEL = 0;

    static final int OPT_IDLE = 1;

    static final int OPT_INFO = 2;

    public void cmdProc(Interp interp, TclObject argv[]) throws TclException {
        int i;
        Notifier notifier = (Notifier) interp.getNotifier();
        Object info;
        if (assocData == null) {
            assocData = (AfterAssocData) interp.getAssocData("tclAfter");
            if (assocData == null) {
                assocData = new AfterAssocData();
                interp.setAssocData("tclAfter", assocData);
            }
        }
        if (argv.length < 2) {
            throw new TclNumArgsException(interp, 1, argv, "option ?arg arg ...?");
        }
        boolean isNumber = false;
        int ms = 0;
        if (argv[1].isIntType()) {
            ms = TclInteger.get(interp, argv[1]);
            isNumber = true;
        } else {
            String s = argv[1].toString();
            if ((s.length() > 0) && (Character.isDigit(s.charAt(0)))) {
                ms = TclInteger.get(interp, argv[1]);
                isNumber = true;
            }
        }
        if (isNumber) {
            if (ms < 0) {
                ms = 0;
            }
            if (argv.length == 2) {
                long endTime = System.currentTimeMillis() + ms;
                while (true) {
                    try {
                        Thread.sleep(ms);
                        return;
                    } catch (InterruptedException e) {
                        long sysTime = System.currentTimeMillis();
                        if (sysTime >= endTime) {
                            return;
                        }
                        ms = (int) (endTime - sysTime);
                        continue;
                    }
                }
            }
            TclObject cmd = getCmdObject(argv);
            cmd.preserve();
            assocData.lastAfterId++;
            TimerInfo timerInfo = new TimerInfo(notifier, ms);
            timerInfo.interp = interp;
            timerInfo.command = cmd;
            timerInfo.id = assocData.lastAfterId;
            assocData.handlers.add(timerInfo);
            interp.setResult("after#" + timerInfo.id);
            return;
        }
        int index;
        try {
            index = TclIndex.get(interp, argv[1], validOpts, "option", 0);
        } catch (TclException e) {
            throw new TclException(interp, "bad argument \"" + argv[1] + "\": must be cancel, idle, info, or a number");
        }
        switch(index) {
            case OPT_CANCEL:
                if (argv.length < 3) {
                    throw new TclNumArgsException(interp, 2, argv, "id|command");
                }
                TclObject arg = getCmdObject(argv);
                arg.preserve();
                info = null;
                for (i = 0; i < assocData.handlers.size(); i++) {
                    Object obj = assocData.handlers.get(i);
                    if (obj instanceof TimerInfo) {
                        TclObject cmd = ((TimerInfo) obj).command;
                        if ((cmd == arg) || cmd.toString().equals(arg.toString())) {
                            info = obj;
                            break;
                        }
                    } else {
                        TclObject cmd = ((IdleInfo) obj).command;
                        if ((cmd == arg) || cmd.toString().equals(arg.toString())) {
                            info = obj;
                            break;
                        }
                    }
                }
                if (info == null) {
                    info = getAfterEvent(interp, arg.toString());
                }
                arg.release();
                if (info != null) {
                    if (info instanceof TimerInfo) {
                        TimerInfo ti = (TimerInfo) info;
                        ti.cancel();
                        ti.command.release();
                    } else {
                        IdleInfo ii = (IdleInfo) info;
                        ii.cancel();
                        ii.command.release();
                    }
                    int hindex = assocData.handlers.indexOf(info);
                    if (hindex == -1) {
                        throw new TclRuntimeError("info " + info + " has no handler");
                    }
                    if (assocData.handlers.remove(hindex) == null) {
                        throw new TclRuntimeError("cound not remove handler " + hindex);
                    }
                }
                break;
            case OPT_IDLE:
                if (argv.length < 3) {
                    throw new TclNumArgsException(interp, 2, argv, "script script ...");
                }
                TclObject cmd = getCmdObject(argv);
                cmd.preserve();
                assocData.lastAfterId++;
                IdleInfo idleInfo = new IdleInfo(notifier);
                idleInfo.interp = interp;
                idleInfo.command = cmd;
                idleInfo.id = assocData.lastAfterId;
                assocData.handlers.add(idleInfo);
                interp.setResult("after#" + idleInfo.id);
                break;
            case OPT_INFO:
                if (argv.length == 2) {
                    TclObject list = TclList.newInstance();
                    for (i = 0; i < assocData.handlers.size(); i++) {
                        int id;
                        Object obj = assocData.handlers.get(i);
                        if (obj instanceof TimerInfo) {
                            id = ((TimerInfo) obj).id;
                        } else {
                            id = ((IdleInfo) obj).id;
                        }
                        TclList.append(interp, list, TclString.newInstance("after#" + id));
                    }
                    interp.setResult(list);
                    return;
                }
                if (argv.length != 3) {
                    throw new TclNumArgsException(interp, 2, argv, "?id?");
                }
                info = getAfterEvent(interp, argv[2].toString());
                if (info == null) {
                    throw new TclException(interp, "event \"" + argv[2] + "\" doesn't exist");
                }
                TclObject list = TclList.newInstance();
                TclList.append(interp, list, ((info instanceof TimerInfo) ? ((TimerInfo) info).command : ((IdleInfo) info).command));
                TclList.append(interp, list, TclString.newInstance((info instanceof TimerInfo) ? "timer" : "idle"));
                interp.setResult(list);
                break;
        }
    }

    private TclObject getCmdObject(TclObject argv[]) throws TclException {
        if (argv.length == 3) {
            return argv[2];
        } else {
            TclObject cmd = Util.concat(2, argv.length - 1, argv);
            return cmd;
        }
    }

    private Object getAfterEvent(Interp interp, String string) {
        if (!string.startsWith("after#")) {
            return null;
        }
        StrtoulResult res = interp.strtoulResult;
        Util.strtoul(string, 6, 10, res);
        if (res.errno != 0) {
            return null;
        }
        int id = (int) res.value;
        for (int i = 0; i < assocData.handlers.size(); i++) {
            Object obj = assocData.handlers.get(i);
            if (obj instanceof TimerInfo) {
                if (((TimerInfo) obj).id == id) {
                    return obj;
                }
            } else {
                if (((IdleInfo) obj).id == id) {
                    return obj;
                }
            }
        }
        return null;
    }

    class AfterAssocData implements AssocData {

        ArrayList handlers = new ArrayList();

        int lastAfterId = 0;

        public void disposeAssocData(Interp interp) {
            for (int i = assocData.handlers.size() - 1; i >= 0; i--) {
                Object info = assocData.handlers.get(i);
                if (assocData.handlers.remove(i) == null) {
                    throw new TclRuntimeError("cound not remove handler " + i);
                }
                if (info instanceof TimerInfo) {
                    TimerInfo ti = (TimerInfo) info;
                    ti.cancel();
                    ti.command.release();
                } else {
                    IdleInfo ii = (IdleInfo) info;
                    ii.cancel();
                    ii.command.release();
                }
            }
            assocData = null;
        }
    }

    class TimerInfo extends TimerHandler {

        Interp interp;

        TclObject command;

        int id;

        TimerInfo(Notifier n, int milliseconds) {
            super(n, milliseconds);
        }

        public void processTimerEvent() {
            try {
                int index = assocData.handlers.indexOf(this);
                if (index == -1) {
                    throw new TclRuntimeError("this " + this + " has no handler");
                }
                if (assocData.handlers.remove(index) == null) {
                    throw new TclRuntimeError("cound not remove handler " + index);
                }
                interp.eval(command, TCL.EVAL_GLOBAL);
            } catch (TclException e) {
                interp.addErrorInfo("\n    (\"after\" script)");
                interp.backgroundError();
            } finally {
                command.release();
                command = null;
            }
        }

        public String toString() {
            StringBuffer sb = new StringBuffer(64);
            sb.append(super.toString());
            sb.append("AfterCmd.TimerInfo : " + command + "\n");
            return sb.toString();
        }
    }

    class IdleInfo extends IdleHandler {

        Interp interp;

        TclObject command;

        int id;

        IdleInfo(Notifier n) {
            super(n);
        }

        public void processIdleEvent() {
            try {
                int index = assocData.handlers.indexOf(this);
                if (index == -1) {
                    throw new TclRuntimeError("this " + this + " has no handler");
                }
                if (assocData.handlers.remove(index) == null) {
                    throw new TclRuntimeError("cound not remove handler " + index);
                }
                interp.eval(command, TCL.EVAL_GLOBAL);
            } catch (TclException e) {
                interp.addErrorInfo("\n    (\"after\" script)");
                interp.backgroundError();
            } finally {
                command.release();
                command = null;
            }
        }

        public String toString() {
            StringBuffer sb = new StringBuffer(64);
            sb.append(super.toString());
            sb.append("AfterCmd.IdleInfo : " + command + "\n");
            return sb.toString();
        }
    }
}
