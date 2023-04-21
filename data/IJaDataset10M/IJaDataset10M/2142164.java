package org.xmlsh.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.xmlsh.util.StringPair;
import org.xmlsh.util.Util;

public class Options {

    public static class OptionDef {

        public String name;

        public String longname;

        public boolean hasArgs;

        public boolean hasMulti;

        public boolean hasPlus;

        public OptionDef(String name, String longname, boolean arg, boolean multi, boolean plus) {
            this.name = name;
            this.longname = longname;
            this.hasArgs = arg;
            this.hasMulti = multi;
            this.hasPlus = plus;
        }
    }

    public static class OptionValue {

        private OptionDef option;

        private boolean optflag = true;

        private List<XValue> values = new ArrayList<XValue>();

        OptionValue(OptionDef def, boolean flag) {
            option = def;
            optflag = flag;
        }

        void setValue(XValue v) {
            if (!option.hasMulti) values.clear();
            values.add(v);
        }

        void addValue(XValue v) {
            values.add(v);
        }

        /**
		 * @return the option
		 */
        public OptionDef getOptionDef() {
            return option;
        }

        /**
		 * @return the arg
		 */
        public XValue getValue() {
            return values.get(0);
        }

        public List<XValue> getValues() {
            return values;
        }

        public boolean getFlag() {
            return optflag;
        }
    }

    private List<OptionDef> mDefs;

    private List<XValue> mRemainingArgs;

    private List<OptionValue> mOptions;

    private boolean mDashDash = false;

    public static List<OptionDef> parseDefs(String sdefs) {
        ArrayList<OptionDef> defs = new ArrayList<OptionDef>();
        String[] adefs = sdefs.trim().split("\\s*,\\s*");
        for (String sdef : adefs) {
            boolean bHasArgs = false;
            boolean bHasMulti = false;
            boolean bPlus = false;
            if (sdef.startsWith("+")) {
                bPlus = true;
                sdef = sdef.substring(1);
            } else if (sdef.endsWith(":")) {
                sdef = sdef.substring(0, sdef.length() - 1);
                bHasArgs = true;
            } else if (sdef.endsWith(":+")) {
                sdef = sdef.substring(0, sdef.length() - 2);
                bHasArgs = true;
                bHasMulti = true;
            }
            StringPair pair = new StringPair(sdef, '=');
            if (pair.hasDelim()) defs.add(new OptionDef(pair.getLeft(), pair.getRight(), bHasArgs, bHasMulti, bPlus)); else defs.add(new OptionDef(sdef, null, bHasArgs, bHasMulti, bPlus));
        }
        return defs;
    }

    public Options(String options) {
        this(parseDefs(options));
    }

    public Options(List<OptionDef> options) {
        mDefs = options;
    }

    private Options(List<OptionDef> opt1, List<OptionDef> opt2) {
        if (opt2 == null) mDefs = opt1; else {
            mDefs = new ArrayList<OptionDef>(opt1.size() + opt2.size());
            addOptionDefs(opt1);
            addOptionDefs(opt2);
        }
    }

    public Options(String option_str, List<OptionDef> option_list) {
        this(parseDefs(option_str), option_list);
    }

    public List<OptionDef> addOptionDefs(String option_str) {
        List<OptionDef> option_list = parseDefs(option_str);
        addOptionDefs(option_list);
        return option_list;
    }

    public void addOptionDefs(List<OptionDef> option_list) {
        for (OptionDef def : option_list) {
            OptionDef exists = getOptDef(def.name);
            if (exists != null) mDefs.remove(exists);
            mDefs.add(def);
        }
    }

    public OptionDef getOptDef(String str) {
        if (mDefs == null) return null;
        for (OptionDef opt : mDefs) {
            if (Util.isEqual(str, opt.name) || Util.isEqual(str, opt.longname)) return opt;
        }
        return null;
    }

    public List<OptionValue> parse(List<XValue> args) throws UnknownOption {
        if (mOptions != null) return mOptions;
        mOptions = new ArrayList<OptionValue>();
        for (Iterator<XValue> I = args.iterator(); I.hasNext(); ) {
            XValue arg = I.next();
            String sarg = (arg.isAtomic() ? arg.toString() : null);
            if (sarg != null && (sarg.startsWith("-") || sarg.startsWith("+")) && !sarg.equals("--") && !Util.isInt(sarg, true)) {
                String a = sarg.substring(1);
                char flag = sarg.charAt(0);
                OptionDef def = getOptDef(a);
                if (def == null) throw new UnknownOption("Unknown option: " + a);
                if (flag == '+' && !def.hasPlus) throw new UnknownOption("Option : " + a + " cannot start with +");
                OptionValue ov = this.getOpt(def);
                boolean bReuse = (ov != null);
                if (ov != null && !def.hasMulti) throw new UnknownOption("Unexpected multiple use of option: " + arg);
                if (ov == null) ov = new OptionValue(def, flag == '-');
                ov.option = def;
                if (def.hasArgs) {
                    if (!I.hasNext()) throw new UnknownOption("Option has no args: " + arg);
                    if (def.hasMulti) ov.addValue(I.next()); else ov.setValue(I.next());
                }
                if (!bReuse) mOptions.add(ov);
            } else {
                mRemainingArgs = new ArrayList<XValue>();
                if (arg.isAtomic() && arg.equals("--")) {
                    arg = null;
                    mDashDash = true;
                }
                if (arg != null) mRemainingArgs.add(arg);
                while (I.hasNext()) mRemainingArgs.add(I.next());
                break;
            }
        }
        return mOptions;
    }

    public List<OptionValue> getOpts() {
        return mOptions;
    }

    public OptionValue getOpt(OptionDef def) {
        for (OptionValue ov : mOptions) {
            if (ov.option == def) return ov;
        }
        return null;
    }

    public OptionValue getOpt(String opt) {
        for (OptionValue ov : mOptions) {
            if (Util.isEqual(opt, ov.getOptionDef().name) || Util.isEqual(opt, ov.getOptionDef().longname)) return ov;
        }
        return null;
    }

    public boolean hasOpt(String opt) {
        return getOpt(opt) != null;
    }

    public boolean getOptFlag(String opt, boolean defValue) {
        OptionValue value = getOpt(opt);
        if (value == null) return defValue; else return value.getFlag();
    }

    public String getOptString(String opt, String defValue) {
        OptionValue value = getOpt(opt);
        if (value != null) return value.getValue().toString(); else return defValue;
    }

    public String getOptStringRequired(String opt) throws InvalidArgumentException {
        OptionValue value = getOpt(opt);
        if (value != null) return value.getValue().toString();
        throw new InvalidArgumentException("Required option: -" + opt);
    }

    public boolean getOptBool(String opt, boolean defValue) {
        OptionValue value = getOpt(opt);
        if (value != null) try {
            return value.getValue().toBoolean();
        } catch (Exception e) {
            return false;
        }
        return defValue;
    }

    public List<XValue> getRemainingArgs() {
        if (mRemainingArgs == null) mRemainingArgs = new ArrayList<XValue>(0);
        return mRemainingArgs;
    }

    public XValue getOptValue(String arg) {
        OptionValue ov = getOpt(arg);
        if (ov == null) return null; else return ov.values.get(0);
    }

    public XValue getOptValueRequired(String arg) throws InvalidArgumentException {
        OptionValue ov = getOpt(arg);
        if (ov != null) return ov.values.get(0);
        throw new InvalidArgumentException("Required option: -" + arg);
    }

    public List<XValue> getOptValuesRequired(String arg) throws InvalidArgumentException {
        OptionValue ov = getOpt(arg);
        if (ov != null) return ov.values;
        throw new InvalidArgumentException("Required option: -" + arg);
    }

    public List<XValue> getOptValues(String arg) throws InvalidArgumentException {
        OptionValue ov = getOpt(arg);
        if (ov != null) return ov.values; else return null;
    }

    public boolean hasRemainingArgs() {
        return mRemainingArgs != null && !mRemainingArgs.isEmpty();
    }

    public double getOptDouble(String opt, double def) {
        return Util.parseDouble(this.getOptString(opt, ""), def);
    }

    public int getOptInt(String opt, int def) {
        return Util.parseInt(this.getOptString(opt, ""), def);
    }

    public long getOptLong(String opt, int def) {
        return Util.parseLong(this.getOptString(opt, ""), def);
    }

    public boolean hasDashDash() {
        return mDashDash;
    }

    /**
	 * @return the defs
	 */
    public List<OptionDef> getOptDefs() {
        return mDefs;
    }
}
