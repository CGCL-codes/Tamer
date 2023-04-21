package pspdash.data;

class InterpreterFactory {

    public static DataInterpreter create(Repository r, String inputName, String prefix) {
        InputName n = new InputName(inputName, prefix);
        if (n.value != null && n.value.length() != 0) try {
            String defaultValue = instantiate(n.name, n.value, prefix);
            if (n.hasFlag('s')) defaultValue = "\"" + defaultValue;
            r.maybeCreateValue(n.name, defaultValue, prefix);
        } catch (RemoteException e) {
        }
        boolean readOnly = n.hasFlag('r');
        DataInterpreter result = null;
        if (n.hasFlag('s')) result = new StringInterpreter(r, n.name, readOnly); else if (n.hasFlag('n')) result = new DoubleInterpreter(r, n.name, n.digitFlag(), readOnly); else if (n.hasFlag('%') || n.name.indexOf('%') != -1) result = new PercentInterpreter(r, n.name, n.digitFlag(), readOnly); else if (n.hasFlag('d')) result = new DateInterpreter(r, n.name, readOnly); else result = new DoubleInterpreter(r, n.name, n.digitFlag(), readOnly);
        boolean optional = !(result instanceof DoubleInterpreter);
        if (n.hasFlag('o')) optional = true;
        if (n.hasFlag('m')) optional = false;
        result.optional = optional;
        if (n.hasFlag('u')) result.unlock();
        if (n.hasFlag('!')) result.setActive(true);
        return result;
    }

    private static String instantiate(String name, String defaultValue, String prefix) {
        StringBuffer val = new StringBuffer();
        String digits;
        int pos;
        if (name.startsWith(prefix + "/")) name = name.substring(prefix.length() + 1);
        pos = name.length();
        while ((pos > 0) && (Character.isDigit(name.charAt(pos - 1)))) pos--;
        digits = name.substring(pos);
        while ((pos = defaultValue.indexOf('=')) != -1) {
            val.append(defaultValue.substring(0, pos));
            switch(defaultValue.charAt(pos + 1)) {
                case '#':
                    val.append(digits);
                    break;
                case 'p':
                    val.append(prefix);
                    break;
                case 'n':
                    val.append(name);
                    break;
            }
            ;
            defaultValue = defaultValue.substring(pos + 2);
        }
        val.append(defaultValue);
        return val.toString();
    }
}
