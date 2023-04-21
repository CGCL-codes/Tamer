package Galaxy.Visitor.Local;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import CLInterface.ConverterConfig;
import CLInterface.ConverterConstants;
import Core.Option;
import Core.Pair;
import Core.Triple;
import Galaxy.Tree.Tool.Tool;
import Galaxy.Tree.Tool.Command.Command;
import Galaxy.Tree.Tool.Command.ParsedCommand;
import Galaxy.Tree.Tool.Input.Input;
import Galaxy.Tree.Tool.Input.Inputs;
import Galaxy.Tree.Tool.Input.Control.Conditional;
import Galaxy.Tree.Tool.Input.Control.Page;
import Galaxy.Tree.Tool.Input.Control.Repeat;
import Galaxy.Tree.Tool.Input.Control.When;
import Galaxy.Tree.Tool.Input.Param.Parameter;
import Galaxy.Tree.Tool.Input.Sanitize.Sanitizer;
import Galaxy.Tree.Tool.Option.Filter;
import Galaxy.Tree.Tool.Option.Options;
import Galaxy.Tree.Tool.Option.Validator;
import Galaxy.Tree.Tool.Output.Data;
import Galaxy.Tree.Tool.Output.Output;
import Galaxy.Tree.Tool.Output.Outputs;
import Galaxy.Tree.Tool.Requirements.Requirement;
import Galaxy.Tree.Tool.Requirements.Requirements;
import Galaxy.Tree.Workflow.ToolState.Primitive;
import Galaxy.Visitor.DFSToolVisitor;
import Galaxy.Visitor.util.LoniEnvironment;
import Galaxy.Visitor.util.LoniContext;
import Galaxy.Visitor.util.ToolStateContext;
import LONI.tree.GraphObject.GraphObject;
import LONI.tree.GraphObject.Module;
import LONI.tree.module.Value;
import LONI.tree.module.format.FileType;
import LONI.tree.module.format.Format;
import Specification.GalaxySpecification;

public class GalaxyToLoniToolConverter extends DFSToolVisitor {

    final String DELIM = "&#xA;";

    {
        toolVisitor = new ToolVisitor() {

            Map<String, String> galaxyLoniMap = null;

            private void loadMap() {
                galaxyLoniMap = new TreeMap<String, String>();
                galaxyLoniMap.put("Flymine", "RemoteDataSource");
            }

            public Pair<Module, LoniEnvironment> visit(Tool tool, LoniContext context) {
                if (galaxyLoniMap == null) loadMap();
                Module o;
                String mapping = galaxyLoniMap.get(tool.getId());
                if (mapping == null) return visitBasicTool(tool, context); else if (mapping.equals("LocalDataSource")) return visitLocalDataSource(tool, context); else if (mapping.equals("RemoteDataSource")) return visitRemoteDataSource(tool, context); else if (mapping.equals("WebServiceModule")) return visitWebServiceModule(tool, context);
                return null;
            }

            public Pair<Module, LoniEnvironment> visitLocalDataSource(Tool tool, LoniContext context) {
                return null;
            }

            public Pair<Module, LoniEnvironment> visitRemoteDataSource(Tool tool, LoniContext context) {
                return null;
            }

            public Pair<Module, LoniEnvironment> visitWebServiceModule(Tool tool, LoniContext context) {
                return null;
            }

            public Pair<Module, LoniEnvironment> visitBasicTool(Tool tool, LoniContext context) {
                Module genModule = new Module();
                LoniEnvironment stepEnv = new LoniEnvironment();
                LoniContext childContext = context.copy();
                childContext.setToolId(tool.getId());
                childContext.getPathContext().addContext("Tool");
                Pair<Object, LoniEnvironment> cmdPair;
                cmdPair = toolImplVisitor.visit(tool.getToolCommand(), childContext.copy());
                stepEnv.addEnvironment(cmdPair.getElem2());
                Module cmdModule = (Module) cmdPair.getElem1();
                Pair<Integer, Integer> pos = context.getPositionContext().getAbsoluteContext(new Pair<Integer, Integer>(0, 0));
                genModule.setPosX(pos.getElem1());
                genModule.setPosY(pos.getElem2());
                genModule.setDescription("Tool Description: " + tool.getDescription());
                genModule.setId(context.getPathContext().getAbsoluteContext(tool.getId()));
                genModule.setName(tool.getFullName());
                genModule.setMyPackage(GalaxySpecification.getDatabase().getPackage(tool.getId()));
                genModule.setVersion(tool.getVersion());
                genModule.setExecutableVersion(tool.getVersion());
                genModule.setLocation(cmdModule.getLocation());
                genModule.setRotation(4);
                if (tool.getToolRequirements() != null) {
                    String reqString = DELIM + "//Requirements//" + DELIM;
                    for (Requirements req : tool.getToolRequirements()) {
                        Pair<Object, LoniEnvironment> le = toolImplVisitor.visit(req, childContext);
                        reqString += (String) le.getElem1();
                    }
                    genModule.setDescription(genModule.getDescription() + reqString);
                }
                if (tool.getHelp() != null) genModule.setDescription(genModule.getDescription() + DELIM + "//HELP//" + DELIM + tool.getHelp());
                Pair<Object, LoniEnvironment> inputResult;
                Pair<Object, LoniEnvironment> outputResult;
                inputResult = (Pair<Object, LoniEnvironment>) toolImplVisitor.visit(tool.getToolInputs(), childContext.copy());
                stepEnv.addEnvironment(inputResult.getElem2());
                outputResult = (Pair<Object, LoniEnvironment>) toolImplVisitor.visit(tool.getToolOutputs(), childContext.copy());
                stepEnv.addEnvironment(outputResult.getElem2());
                List<LONI.tree.module.Parameter> myparams = (List<LONI.tree.module.Parameter>) inputResult.getElem1();
                List<LONI.tree.module.Output> myoutputs = (List<LONI.tree.module.Output>) outputResult.getElem1();
                for (LONI.tree.module.Parameter param : cmdModule.getInputs()) {
                    LONI.tree.module.Parameter myinput = null;
                    LONI.tree.module.Output myoutput = null;
                    for (LONI.tree.module.Parameter inputParam : myparams) {
                        if (param.getName().equals(inputParam.getName())) {
                            myinput = inputParam;
                            break;
                        }
                    }
                    for (LONI.tree.module.Output outputParam : myoutputs) {
                        if (param.getName().equals(outputParam.getName())) {
                            myoutput = outputParam;
                            break;
                        }
                    }
                    if (myinput != null) {
                        myinput.setOrder(param.getOrder());
                        myinput.setPrefix(param.getPrefix());
                    } else if (myoutput != null) {
                        myoutput.setOrder(param.getOrder());
                    } else {
                        myparams.add(param);
                    }
                }
                genModule.addOutputs(myoutputs);
                genModule.addInputs(myparams);
                return new Pair<Module, LoniEnvironment>(genModule, stepEnv);
            }
        };
    }

    {
        toolImplVisitor = new ToolImplVisitor() {

            /***
			 * Parses a command line into a structure.
			 * @param cmd string of unix/python/perl command to parse.
			 * @return returns a <position, prefix, info> where info is the parameter name and parameter value
			 * if the parameter has no value, return null in that field.
			 */
            private List<Triple<Integer, String, Pair<String, String>>> parseCommand(String cmd) {
                List<Triple<Integer, String, Pair<String, String>>> cmdData;
                cmdData = new LinkedList<Triple<Integer, String, Pair<String, String>>>();
                String[] args = cmd.split("[ 	\r\n=]");
                int idx = 0;
                String prefix = "";
                for (String arg : args) {
                    if (arg.length() == 0) {
                    } else if (arg.charAt(0) == '-') {
                        prefix = arg;
                    } else if (arg.charAt(0) == '$') {
                        arg = arg.replace("$", "");
                        cmdData.add(new Triple(idx, prefix, new Pair(arg, null)));
                        prefix = "";
                        idx++;
                    } else {
                        String name = "Const" + idx;
                        cmdData.add(new Triple(idx, prefix, new Pair(name, arg)));
                        prefix = "";
                        idx++;
                    }
                }
                return cmdData;
            }

            public Pair<Object, LoniEnvironment> visit(Command command, LoniContext context) {
                LoniEnvironment stepEnv = new LoniEnvironment();
                LoniContext childContext = context.copy();
                Module genModule = new Module();
                List<LONI.tree.module.Parameter> myparams = new ArrayList<LONI.tree.module.Parameter>();
                if (command.getInterpereter() != null && command.getInterpereter() != "") {
                    String interp = command.getInterpereter();
                    String ABS_PATH = "";
                    if (interp.equals("python")) {
                        ABS_PATH = ConverterConfig.PYTHON_BIN;
                    } else if (interp.equals("perl")) {
                        ABS_PATH = ConverterConfig.PERL_BIN;
                    } else if (interp.equals("bash")) {
                        ABS_PATH = ConverterConfig.BASH_BIN;
                    } else ABS_PATH = "";
                    genModule.setLocation(ConverterConstants.LOCALHOST_PATH + ABS_PATH);
                } else {
                    String cmd = command.getCommand();
                    genModule.setLocation(ConverterConstants.PIPELINE_PATH + ConverterConstants.LOCALHOST_PATH + command.getCommand().split(" ")[0]);
                }
                List<Triple<Integer, String, Pair<String, String>>> commandData = parseCommand(command.getCommand());
                for (Triple<Integer, String, Pair<String, String>> arg : commandData) {
                    Format format = new Format();
                    LONI.tree.module.Parameter parameter = new LONI.tree.module.Parameter();
                    int order = arg.getElem1();
                    parameter.setId(context.getPathContext().getAbsoluteContext(arg.getElem3().getElem1()));
                    parameter.setName(arg.getElem3().getElem1());
                    parameter.setOrder(order);
                    parameter.setPrefix(arg.getElem2());
                    parameter.setRequired(true);
                    parameter.setEnabled(true);
                    format.setType("String");
                    format.setCardinality(1);
                    parameter.setFileFormat(format);
                    if (arg.getElem3().getElem2() != null) {
                        Value value;
                        String val = arg.getElem3().getElem2();
                        if (order == 0 && command.getInterpereter() != "") {
                            if (val.charAt(0) != '/') {
                                String toolid = context.getToolId();
                                String dir = GalaxySpecification.getDatabase().getDirectory(toolid);
                                val = "{GALAXY_DIR}" + dir + val;
                            }
                        }
                        value = new Value(null, val);
                        parameter.getValues().addValue(value);
                    }
                    genModule.addInput(parameter);
                }
                return new Pair<Object, LoniEnvironment>(genModule, stepEnv);
            }

            public Pair<Object, LoniEnvironment> visit(Inputs inputs, LoniContext context) {
                LoniEnvironment stepEnv = new LoniEnvironment();
                inputs.getNginxUpload();
                List<LONI.tree.module.Parameter> parameters = new ArrayList<LONI.tree.module.Parameter>();
                for (Input input : inputs.getInputList()) {
                    Pair<Object, LoniEnvironment> param;
                    param = (Pair<Object, LoniEnvironment>) visit(input, context);
                    if (param != null) {
                        if (param.getElem1() != null) {
                            if (param.getElem1() instanceof LONI.tree.module.Parameter) parameters.add((LONI.tree.module.Parameter) param.getElem1()); else if (param.getElem1() instanceof List) parameters.addAll((List<LONI.tree.module.Parameter>) param.getElem1());
                        }
                        stepEnv.addEnvironment(param.getElem2());
                    }
                }
                return new Pair<Object, LoniEnvironment>(parameters, stepEnv);
            }

            public Pair<Object, LoniEnvironment> visit(Outputs outputs, LoniContext context) {
                LoniEnvironment stepEnv = new LoniEnvironment();
                List<LONI.tree.module.Output> parameters = new ArrayList<LONI.tree.module.Output>();
                for (Output output : outputs.getOutputs()) {
                    Pair<Object, LoniEnvironment> param;
                    param = (Pair<Object, LoniEnvironment>) visit(output, context);
                    parameters.add((LONI.tree.module.Output) param.getElem1());
                    stepEnv.addEnvironment(param.getElem2());
                }
                return new Pair<Object, LoniEnvironment>(parameters, stepEnv);
            }

            public Pair<Object, LoniEnvironment> visit(Data data, LoniContext context) {
                LoniEnvironment stepEnv = new LoniEnvironment();
                LoniContext childContext = context.copy();
                String id = context.getPathContext().getAbsoluteContext(data.getName());
                String name = data.getName();
                String description = "";
                boolean required = true;
                boolean enabled = true;
                int order = 0;
                if (data.getFilters() != null) {
                    for (Filter f : data.getFilters()) {
                        Pair<Object, LoniEnvironment> filterPair = visit(f, childContext);
                        stepEnv.addEnvironment(filterPair.getElem2());
                    }
                }
                if (data.getChangeFormat() != null) {
                    Pair<Object, LoniEnvironment> formatPair;
                    formatPair = visit(data.getChangeFormat(), childContext);
                    stepEnv.addEnvironment(formatPair.getElem2());
                }
                String formatType = "File";
                String formatCardinality = "";
                int formatCardinalityBase = 1;
                String formatTransformationBase = "";
                Format format = new Format(formatType, formatCardinalityBase, formatCardinality, formatTransformationBase);
                String ftName = data.getFormat();
                String ftDesc = "";
                String ftExt = "";
                format.getFileTypes().addFileType(new FileType(ftName, ftDesc, ftExt));
                stepEnv.addExternalOutput(data.getName(), id);
                LONI.tree.module.Output output;
                output = new LONI.tree.module.Output(id, name, description, required, enabled, order, format);
                return new Pair<Object, LoniEnvironment>(output, stepEnv);
            }

            public String toLoniString(Primitive primitive) {
                if (primitive.isList()) {
                    Queue<Primitive> pr = primitive.getList();
                    Value v = new Value();
                    String s = "\"";
                    int bound = pr.size() - 1;
                    int i = 0;
                    for (Primitive p : pr) {
                        s += p.toString();
                        if (i < bound) s += ",";
                        i++;
                    }
                    s += "\"";
                    return s;
                } else {
                    Value v = new Value();
                    return ("\"" + primitive.toString() + "\"");
                }
            }

            public Pair<Object, LoniEnvironment> visit(Parameter parameter, LoniContext context) {
                LONI.tree.module.Parameter loniParameter;
                LoniEnvironment stepEnv = new LoniEnvironment();
                LoniContext childContext = context.copy();
                Map<String, String> typeMap = new TreeMap<String, String>() {

                    {
                        put("data", "File");
                        put("select", "Enumerated");
                        put("data_column", "String");
                    }
                };
                String id = context.getPathContext().getAbsoluteContext(parameter.getName());
                loniParameter = new LONI.tree.module.Parameter();
                Format format = new Format();
                FileType filetype = new FileType();
                loniParameter.setId(id);
                loniParameter.setRequired(true);
                loniParameter.setEnabled(true);
                loniParameter.setName(parameter.getName());
                loniParameter.setDescription(parameter.getLabel());
                Option<Primitive, ToolStateContext> stateMap;
                stateMap = context.getStateContext().get(parameter.getName());
                loniParameter.setFileFormat(format);
                if (parameter.getValidators() != null) {
                    for (Validator v : parameter.getValidators()) {
                        Pair<Object, LoniEnvironment> validatorPair = visit(v, childContext);
                        stepEnv.addEnvironment(validatorPair.getElem2());
                    }
                }
                if (parameter.getSanitizers() != null) {
                    for (Sanitizer v : parameter.getSanitizers()) {
                        Pair<Object, LoniEnvironment> sanitizerPair = visit(v, childContext);
                        stepEnv.addEnvironment(sanitizerPair.getElem2());
                    }
                }
                Primitive paramValue = null;
                if (!parameter.getType().equals("data")) {
                    Option<Primitive, ToolStateContext> paramOpt;
                    paramOpt = context.getStateContext().get(parameter.getName());
                    if (paramOpt != null) {
                        if (paramOpt.isFirst()) paramValue = paramOpt.getFirst(); else {
                            paramOpt = paramOpt.getSecond().get("value");
                            if (paramOpt != null && paramOpt.isFirst()) {
                                paramValue = paramOpt.getFirst();
                            }
                        }
                    }
                } else {
                    parameter.getOptionList();
                }
                if (paramValue != null) {
                    Value v = new Value();
                    v.setValue(toLoniString(paramValue));
                    loniParameter.getValues().addValue(v);
                }
                if (parameter.getMultiple() != null && parameter.getMultiple() == true) {
                    format.setCardinality(-1);
                } else {
                    format.setCardinality(1);
                }
                if (typeMap.containsKey(parameter.getType()) && typeMap.get(parameter.getType()).equals("File")) {
                    format.setType(typeMap.get(parameter.getType()));
                    format.getFileTypes().addFileType(filetype);
                    filetype.setExtension("");
                    filetype.setDescription("");
                    filetype.setName(parameter.getFormat());
                } else if (typeMap.containsKey(parameter.getType()) && typeMap.get(parameter.getType()).equals("Enumerated")) {
                    format.setType(typeMap.get(parameter.getType()));
                    if (parameter.getOptionList() != null) {
                        for (Galaxy.Tree.Tool.Input.Param.Option opt : parameter.getOptionList()) {
                            Pair<Object, LoniEnvironment> value = visit(opt, childContext);
                            loniParameter.getValues().addValue((Value) value.getElem1());
                        }
                    }
                    if (parameter.getOptions() != null) {
                        Pair<Object, LoniEnvironment> options = visit(parameter.getOptions(), childContext);
                        List<Value> values = (List<Value>) options.getElem1();
                        if (values.size() == 0 && paramValue != null) {
                            Value v = new Value();
                            v.setValue(toLoniString(paramValue));
                            values.add(v);
                        }
                    }
                } else if (typeMap.containsKey(parameter.getType()) && typeMap.get(parameter.getType()).equals("String")) {
                    format.setType(typeMap.get(parameter.getType()));
                }
                stepEnv.addExternalInput(parameter.getName(), id);
                List<LONI.tree.module.Parameter> loniParameterList = new ArrayList<LONI.tree.module.Parameter>();
                loniParameterList.add(loniParameter);
                return new Pair<Object, LoniEnvironment>(loniParameterList, stepEnv);
            }

            public Pair<Object, LoniEnvironment> visit(Galaxy.Tree.Tool.Input.Param.Option option, LoniContext context) {
                String value = option.getValue();
                String contents = option.getContents();
                Value val = new Value();
                val.setValue(contents);
                val.setMetadata(value);
                return new Pair<Object, LoniEnvironment>(val, new LoniEnvironment());
            }

            public Pair<Object, LoniEnvironment> visit(Conditional conditional, LoniContext context) {
                LoniContext childContext = context.copy();
                LoniEnvironment le = new LoniEnvironment();
                List<LONI.tree.module.Parameter> parameter = new LinkedList<LONI.tree.module.Parameter>();
                String tag1name = conditional.getName();
                String tag2name = conditional.getParameter().getName();
                childContext.getStateContext().stepInto(tag1name);
                childContext.getPathContext().addContext(conditional.getName(), "|");
                Option<Primitive, ToolStateContext> chosenValuePair = childContext.getStateContext().get(tag2name);
                System.out.println(tag1name);
                System.out.println(">" + tag2name);
                System.out.println(childContext.getStateContext().toString());
                String chosenValue = chosenValuePair.getFirst().getString();
                String chosenFormat = null;
                String chosenInput = null;
                Triple<String, String, String> chooseTriple;
                chooseTriple = new Triple<String, String, String>(chosenValue, chosenFormat, chosenInput);
                for (When when : conditional.getConditions()) {
                    Pair<Object, LoniEnvironment> o = visit(when, childContext);
                    Pair<Triple<String, String, String>, List<LONI.tree.module.Parameter>> opts;
                    opts = (Pair<Triple<String, String, String>, List<LONI.tree.module.Parameter>>) o.getElem1();
                    Triple<String, String, String> valTriple = opts.getElem1();
                    if (((valTriple.getElem1() == null || chooseTriple.getElem1() == null) || valTriple.getElem1().equals(chooseTriple.getElem1())) && ((valTriple.getElem2() == null || chooseTriple.getElem2() == null) || valTriple.getElem2().equals(chooseTriple.getElem2())) && ((valTriple.getElem3() == null || chooseTriple.getElem3() == null) || valTriple.getElem3().equals(chooseTriple.getElem3()))) {
                        parameter.addAll(opts.getElem2());
                        if (o.getElem2() != null) {
                            le.addEnvironment(o.getElem2());
                            if (o.getElem2().getInputAliases() != null) {
                                for (String key : o.getElem2().getInputAliases().keySet()) {
                                    String val = o.getElem2().getInputAliases().get(key);
                                    key = conditional.getName() + "|" + key;
                                    le.getInputAliases().put(key, val);
                                }
                            }
                            if (o.getElem2().getOutputAliases() != null) {
                                for (String key : o.getElem2().getOutputAliases().keySet()) {
                                    String val = o.getElem2().getOutputAliases().get(key);
                                    key = conditional.getName() + "|" + key;
                                    le.getOutputAliases().put(key, val);
                                }
                            }
                        }
                    }
                }
                childContext.getStateContext().stepOut();
                return new Pair<Object, LoniEnvironment>(parameter, le);
            }

            public Pair<Object, LoniEnvironment> visit(When when, LoniContext context) {
                LoniContext childContext = context.copy();
                LoniEnvironment loniEnvironment = new LoniEnvironment();
                Pair<Triple<String, String, String>, List<LONI.tree.module.Parameter>> mypair;
                List<LONI.tree.module.Parameter> params = new ArrayList<LONI.tree.module.Parameter>();
                System.out.println(when.getValue());
                if (when.getParameter() != null) {
                    for (Input p : when.getParameter()) {
                        Pair<Object, LoniEnvironment> param = visit(p, childContext);
                        loniEnvironment.addEnvironment(param.getElem2());
                        if (param.getElem1() instanceof LONI.tree.module.Parameter) {
                            params.add((LONI.tree.module.Parameter) param.getElem1());
                        } else if (param.getElem1() instanceof List) {
                            List<LONI.tree.module.Parameter> pl = (List<LONI.tree.module.Parameter>) param.getElem1();
                            params.addAll(pl);
                        }
                    }
                }
                mypair = new Pair<Triple<String, String, String>, List<LONI.tree.module.Parameter>>(new Triple<String, String, String>(when.getValue(), when.getInput(), when.getFormat()), params);
                return new Pair<Object, LoniEnvironment>(mypair, loniEnvironment);
            }

            public Pair<Object, LoniEnvironment> visit(Repeat p, LoniContext context) {
                LoniContext childContext = context.copy();
                LoniEnvironment lenv = new LoniEnvironment();
                List<Parameter> params = new LinkedList<Parameter>();
                childContext.getPathContext().addContext(p.getName());
                for (Input input : p.getInputs()) {
                    Pair<Object, LoniEnvironment> objPair;
                    objPair = visit(input, childContext);
                    params.addAll((List<Parameter>) objPair.getElem1());
                    lenv.addEnvironment(objPair.getElem2());
                }
                return new Pair<Object, LoniEnvironment>(params, lenv);
            }

            public Pair<Object, LoniEnvironment> visit(Page p, LoniContext context) {
                LoniContext childContext = context.copy();
                LoniEnvironment le = new LoniEnvironment();
                List<Parameter> lp = new LinkedList<Parameter>();
                childContext.getPathContext().addContext("Page");
                if (p.getParameters() != null) {
                    for (Input param : p.getParameters()) {
                        Pair<Object, LoniEnvironment> paramPair = visit(param, childContext);
                        lp.addAll((List<Parameter>) paramPair.getElem1());
                        le.addEnvironment(paramPair.getElem2());
                    }
                }
                return new Pair<Object, LoniEnvironment>(lp, le);
            }

            public Pair<Object, LoniEnvironment> visit(Requirement p, LoniContext context) {
                String str = p.getType() + ": " + p.getValue();
                return new Pair<Object, LoniEnvironment>(str, new LoniEnvironment());
            }

            public Pair<Object, LoniEnvironment> visit(Requirements p, LoniContext context) {
                LoniContext childContext = context.copy();
                LoniEnvironment le = new LoniEnvironment();
                String req = "";
                if (p.getRequirements() != null) {
                    for (Requirement r : p.getRequirements()) {
                        Pair<Object, LoniEnvironment> ret = visit(r, childContext);
                        req += ret.getElem1() + " // ";
                        le.addEnvironment(ret.getElem2());
                    }
                }
                return new Pair<Object, LoniEnvironment>(req, le);
            }

            public Pair<Object, LoniEnvironment> visit(Options options, LoniContext context) {
                return new Pair<Object, LoniEnvironment>(new ArrayList<String>(), new LoniEnvironment());
            }

            public Pair<Object, LoniEnvironment> visit(Validator validator, LoniContext context) {
                return new Pair<Object, LoniEnvironment>(null, new LoniEnvironment());
            }

            public Pair<Object, LoniEnvironment> visit(Sanitizer sanitizer, LoniContext context) {
                return new Pair<Object, LoniEnvironment>(null, new LoniEnvironment());
            }
        };
    }
}
