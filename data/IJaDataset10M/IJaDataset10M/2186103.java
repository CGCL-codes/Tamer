package pcode;

import java.io.File;
import jaguar.Jaguar;
import jaguar.JaguarData;
import jaguar.JaguarPCode;
import jaguar.JaguarProcess;
import jaguar.JaguarVM;
import jaguar.JaguarVariable;

/**
 * @author peter
 *
 */
public abstract class JaguarIO extends JaguarPCode {

    /**
	 * @param vm
	 * @param src
	 * @param line
	 * @param arg
	 */
    public JaguarIO(JaguarVM vm, File src, String line, String arg) {
        super(vm, src, line, arg);
    }

    protected void close(String name) {
        for (int i = 0; i < vm.getDataSize(); i++) {
            JaguarData d = vm.getData(i);
            if (d.getName().equals(name)) {
                if (d.isOutput()) d.flush();
                d.close();
                vm.removeData(i);
                --i;
            }
        }
    }

    /**
	 * @param name - String
	 * @return handle for appending JaguarData
	 * @throws Exception
	 */
    protected JaguarData getAppendData(String name) throws Exception {
        JaguarData d = null;
        for (int i = 0; i < vm.getDataSize(); i++) {
            d = vm.getData(i);
            if (d.getName().equals(name)) {
                if (d.isOutput()) {
                    d.flush();
                    return d;
                } else {
                    d.close();
                    vm.removeData(i);
                    --i;
                }
            }
        }
        JaguarProcess pipe = Jaguar.getTask(name);
        d = new JaguarData(name, false, pipe);
        d.append();
        vm.addData(d);
        return d;
    }

    /**
	 * @param name - String
	 * @return handle for reading JaguarData
	 * @throws Exception
	 */
    protected JaguarData getReadData(String name) throws Exception {
        JaguarData d = null;
        for (int i = 0; i < vm.getDataSize(); i++) {
            d = vm.getData(i);
            if (d.getName().equals(name)) {
                if (d.isOutput()) {
                    d.flush();
                    d.close();
                    vm.removeData(i);
                    --i;
                } else return d;
            }
        }
        JaguarProcess pipe = Jaguar.getTask(name);
        d = new JaguarData(name, false, pipe);
        vm.addData(d);
        return d;
    }

    /**
	 * @param name - String with the filename
	 * @return handle for writing JaguarData
	 * @throws Exception
	 */
    protected JaguarData getWriteData(String name) throws Exception {
        JaguarData d = null;
        for (int i = 0; i < vm.getDataSize(); i++) {
            d = vm.getData(i);
            if (d.getName().equals(name)) {
                if (!d.isOutput()) {
                    d.close();
                    vm.removeData(i);
                    --i;
                } else return d;
            }
        }
        JaguarProcess pipe = Jaguar.getTask(name);
        d = new JaguarData(name, true, pipe);
        String s;
        if (getClass() == JaguarOPEN.class) s = getArgTail(1).replaceAll("\\s+", " "); else s = getArg().replaceAll("\\s+", " ");
        if (name.toLowerCase().endsWith(".html") || name.toLowerCase().endsWith(".htm")) d.setHtml();
        int i = s.indexOf(' ');
        String[] head;
        if (i < 0) {
            for (i = 0; i < vm.getVarsSize(); i++) {
                JaguarVariable lab = vm.getVar(i);
                if (lab.getName() != null) break;
            }
            head = new String[vm.getVarsSize() - i];
            for (int j = i; j < vm.getVarsSize(); j++) {
                JaguarVariable lab = vm.getVar(j);
                head[j - i] = lab.getName();
            }
        } else {
            head = s.substring(i).trim().split("\\s+");
        }
        d.writeHead(head);
        vm.addData(d);
        return d;
    }
}
