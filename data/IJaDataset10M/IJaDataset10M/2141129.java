package br.com.linkcom.neo.view.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import br.com.linkcom.neo.view.ComboReloadGroupTag;

public class FunctionCall {

    String object;

    String functionName;

    String parameters;

    String call;

    public String getCall() {
        return call;
    }

    public FunctionCall(String call) {
        this.call = call.trim();
        Pattern pattern1 = Pattern.compile("(\\w*)\\s*?\\.(\\w*)\\s*?((\\(.*?\\)))");
        Matcher matcher1 = pattern1.matcher(call);
        if (matcher1.find()) {
            System.out.println("found " + call);
            this.object = matcher1.group(1);
            this.functionName = matcher1.group(2);
            this.parameters = matcher1.group(3);
        } else {
            Pattern pattern2 = Pattern.compile("(\\w*)\\s*?\\.?(\\w*)\\s*?");
            Matcher matcher2 = pattern2.matcher(call);
            if (matcher2.find()) {
                System.out.println("found " + call);
                this.object = matcher2.group(1);
                this.functionName = matcher2.group(2);
                this.parameters = "";
            } else {
                throw new RuntimeException("Fun��o fora do padr�o: " + call);
            }
        }
        System.out.println(Arrays.deepToString(getParameterArray()));
    }

    public FunctionParameter[] getParameterArray() {
        List<FunctionParameter> parameters = new ArrayList<FunctionParameter>();
        if (this.parameters == null || this.parameters.trim().length() == 0) {
            return new FunctionParameter[0];
        }
        char[] parameterrCharArray = this.parameters.toCharArray();
        int step = 1;
        StringBuilder currentParam = null;
        boolean aspasDuplas = false;
        ParameterType currentType = null;
        for (int i = 0; i < parameterrCharArray.length; i++) {
            char currentChar = parameterrCharArray[i];
            try {
                if (String.valueOf(currentChar).equals(ComboReloadGroupTag.PARAMETRO_SEPARATOR)) {
                    throw new CaracterInvalidoException();
                }
                switch(step) {
                    case 1:
                        if (currentChar == '(') {
                            step = 2;
                            continue;
                        } else if (currentChar == ' ') {
                            continue;
                        } else {
                            throw new CaracterInvalidoException();
                        }
                    case 2:
                        if (currentParam != null && currentParam.length() > 0) {
                            parameters.add(new FunctionParameter(currentParam.toString().trim(), currentType));
                        }
                        currentParam = new StringBuilder();
                        if (currentChar == ' ') {
                            continue;
                        } else if (currentChar == '\'') {
                            aspasDuplas = false;
                            step = 3;
                            continue;
                        } else if (currentChar == '"') {
                            aspasDuplas = true;
                            step = 3;
                            continue;
                        } else if (currentChar == 't') {
                            step = 11;
                        } else if (currentChar == 'f') {
                            step = 12;
                        } else if (currentChar == 'u') {
                            step = 13;
                        } else if (currentChar == ')') {
                            if (currentParam.toString().trim().length() > 0) {
                                parameters.add(new FunctionParameter(currentParam.toString().trim(), currentType));
                            }
                            step = 6;
                            continue;
                        } else if (String.valueOf(currentChar).matches("[0-9]")) {
                            step = 8;
                        } else if (String.valueOf(currentChar).matches("[A-Za-z]")) {
                            step = 7;
                        } else {
                            throw new CaracterInvalidoException();
                        }
                        break;
                    case 3:
                        currentType = ParameterType.STRING;
                        if (currentChar == '"') {
                            if (aspasDuplas) {
                                step = 5;
                                continue;
                            }
                        } else if (currentChar == '\'') {
                            if (!aspasDuplas) {
                                step = 5;
                                continue;
                            }
                        } else if (currentChar == '\\') {
                            step = 4;
                            continue;
                        }
                        break;
                    case 4:
                        if (currentChar == '\'' || currentChar == '\"' || currentChar == '\\') {
                            step = 3;
                        } else {
                            throw new SequenciaEscapeException();
                        }
                        break;
                    case 5:
                        if (currentChar == ' ') {
                            continue;
                        } else if (currentChar == ',') {
                            step = 2;
                            continue;
                        } else if (currentChar == ')') {
                            parameters.add(new FunctionParameter(currentParam.toString().trim(), currentType));
                            step = 6;
                            continue;
                        } else {
                            throw new CaracterEsperadoException(",");
                        }
                    case 6:
                        break;
                    case 7:
                        currentType = ParameterType.REFERENCE;
                        if (currentChar == ' ') {
                            step = 5;
                            continue;
                        } else if (currentChar == ',') {
                            step = 2;
                            continue;
                        } else if (currentChar == ')') {
                            parameters.add(new FunctionParameter(currentParam.toString().trim(), currentType));
                            step = 6;
                            continue;
                        }
                        break;
                    case 8:
                        currentType = ParameterType.NUMBER;
                        if (currentChar == ' ') {
                            step = 5;
                            continue;
                        } else if (currentChar == ',') {
                            step = 2;
                            continue;
                        } else if (currentChar == ')') {
                            parameters.add(new FunctionParameter(currentParam.toString().trim(), currentType));
                            step = 6;
                            continue;
                        } else if (String.valueOf(currentChar).matches("[A-Za-z]")) {
                            throw new CaracterInvalidoException();
                        }
                        break;
                    case 11:
                        currentType = ParameterType.BOOLEAN;
                        switch(currentParam.length()) {
                            case 1:
                                if (currentChar != 'r') {
                                    step = 7;
                                }
                                break;
                            case 2:
                                if (currentChar != 'u') {
                                    step = 7;
                                }
                                break;
                            case 3:
                                if (currentChar != 'e') {
                                    step = 7;
                                }
                                break;
                            case 4:
                                if (currentChar == ' ') {
                                    step = 5;
                                    continue;
                                } else if (currentChar == ',') {
                                    step = 2;
                                    continue;
                                } else if (currentChar == ')') {
                                    parameters.add(new FunctionParameter(currentParam.toString().trim(), currentType));
                                    step = 6;
                                    continue;
                                } else {
                                    step = 7;
                                }
                        }
                        break;
                    case 12:
                        currentType = ParameterType.BOOLEAN;
                        switch(currentParam.length()) {
                            case 1:
                                if (currentChar != 'a') {
                                    step = 7;
                                }
                                break;
                            case 2:
                                if (currentChar != 'l') {
                                    step = 7;
                                }
                                break;
                            case 3:
                                if (currentChar != 's') {
                                    step = 7;
                                }
                                break;
                            case 4:
                                if (currentChar != 'e') {
                                    step = 7;
                                }
                                break;
                            case 5:
                                if (currentChar == ' ') {
                                    step = 5;
                                    continue;
                                } else if (currentChar == ',') {
                                    step = 2;
                                    continue;
                                } else if (currentChar == ')') {
                                    parameters.add(new FunctionParameter(currentParam.toString().trim(), currentType));
                                    step = 6;
                                    continue;
                                } else {
                                    step = 7;
                                }
                        }
                        break;
                    case 13:
                        currentType = ParameterType.USER;
                        switch(currentParam.length()) {
                            case 1:
                                if (currentChar != 's') {
                                    step = 7;
                                }
                                break;
                            case 2:
                                if (currentChar != 'e') {
                                    step = 7;
                                }
                                break;
                            case 3:
                                if (currentChar != 'r') {
                                    step = 7;
                                }
                                break;
                            case 4:
                                if (currentChar == ' ') {
                                    step = 5;
                                    continue;
                                } else if (currentChar == ',') {
                                    step = 2;
                                    continue;
                                } else if (currentChar == ')') {
                                    parameters.add(new FunctionParameter(currentParam.toString().trim(), currentType));
                                    step = 6;
                                    continue;
                                } else {
                                    step = 7;
                                }
                        }
                        break;
                    default:
                        throw new RuntimeException("Ocorreu um erro inesperado ao fazer parsing da fun��o " + call + " Passo inv�lido: " + step);
                }
                currentParam.append(currentChar);
            } catch (CaracterInvalidoException e) {
                throw new RuntimeException("Fun��o inv�lida: " + call + " caracter '" + currentChar + "' inv�lido na posi��o " + i);
            } catch (SequenciaEscapeException e) {
                throw new RuntimeException("Fun��o inv�lida: " + call + " caracter '" + currentChar + "' inv�lido na posi��o " + i + " Sequencia de escape inv�lida");
            } catch (CaracterEsperadoException e) {
                throw new RuntimeException("Fun��o inv�lida: " + call + " caracter '" + currentChar + "' inv�lido na posi��o " + i + " Caracter esperado: " + e.getMessage());
            }
        }
        return parameters.toArray(new FunctionParameter[parameters.size()]);
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }
}

class CaracterInvalidoException extends Exception {

    private static final long serialVersionUID = 1L;

    public CaracterInvalidoException() {
        super();
    }
}

class SequenciaEscapeException extends Exception {

    private static final long serialVersionUID = 1L;

    public SequenciaEscapeException() {
        super();
    }
}

class CaracterEsperadoException extends Exception {

    private static final long serialVersionUID = 1L;

    public CaracterEsperadoException(String chars) {
        super(chars);
    }
}
