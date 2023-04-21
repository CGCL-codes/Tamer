package com.aptana.ide.editor.js;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.internal.Workbench;
import com.aptana.ide.core.ui.CoreUIUtils;
import com.aptana.ide.editor.js.environment.LexemeBasedEnvironmentLoader;
import com.aptana.ide.editor.js.lexing.JSTokenTypes;
import com.aptana.ide.editor.js.runtime.Environment;
import com.aptana.ide.editor.js.runtime.IFunction;
import com.aptana.ide.editor.js.runtime.IObject;
import com.aptana.ide.editor.js.runtime.IScope;
import com.aptana.ide.editor.js.runtime.JSFunction;
import com.aptana.ide.editor.js.runtime.JSScope;
import com.aptana.ide.editor.js.runtime.ObjectBase;
import com.aptana.ide.editor.js.runtime.OrderedObjectCollection;
import com.aptana.ide.editor.js.runtime.Property;
import com.aptana.ide.editor.scriptdoc.parsing.FunctionDocumentation;
import com.aptana.ide.editor.scriptdoc.parsing.PropertyDocumentation;
import com.aptana.ide.editors.managers.FileContextManager;
import com.aptana.ide.editors.unified.ChildOffsetMapper;
import com.aptana.ide.editors.unified.FileService;
import com.aptana.ide.editors.unified.IChildOffsetMapper;
import com.aptana.ide.editors.unified.IFileService;
import com.aptana.ide.editors.unified.IParentOffsetMapper;
import com.aptana.ide.editors.unified.UnifiedEditor;
import com.aptana.ide.lexer.Lexeme;
import com.aptana.ide.lexer.LexemeList;
import com.aptana.ide.lexer.TokenCategories;
import com.aptana.ide.metadata.IDocumentation;
import com.aptana.ide.parsing.CodeLocation;
import com.aptana.ide.parsing.ICodeLocation;
import com.aptana.ide.parsing.IParseState;

/**
 * @author Robin Debreuil
 */
public class JSOffsetMapper extends ChildOffsetMapper implements IChildOffsetMapper {

    /** MODE_NORMAL for assist type */
    public static final String MODE_NORMAL = "__mode_normal";

    /** MODE_NEW for assist type */
    public static final String MODE_NEW = "__mode_new";

    /** MODE_INVOKING for assist type */
    public static final String MODE_INVOKING = "__mode_invoking";

    /** MODE_INVOKING for assist type */
    public static final String MODE_STRING = "__mode_string";

    /** NOT_AN_IDENTIFIER marker for code assist */
    public static final String NOT_AN_IDENTIFIER = "__not_an_identifier";

    /** NOT_INVOKING marker for code assist */
    public static final String NOT_INVOKING = "__not_invoking";

    /** An undefined object * */
    private static IObject undef = ObjectBase.UNDEFINED;

    private String mode = MODE_NORMAL;

    private JSFileLanguageService fileLangService;

    private LexemeBasedEnvironmentLoader loader;

    private static Environment environment;

    /**
	 * @param parent
	 */
    public JSOffsetMapper(IParentOffsetMapper parent) {
        super(parent);
        if (environment == null) {
            environment = (Environment) JSLanguageEnvironment.getInstance().getRuntimeEnvironment();
        }
    }

    /**
	 * Looks up an object in the global table based on its full name and returns the final object
	 * (not its return type). This uses the special notation (from getNameHash) for full names.
	 * 
	 * @param fullname
	 *            Full name of the object to lookup
	 * @param scope
	 *            The scope to start looking from - this allows lookup from inside a function scope.
	 * @param offset
	 * @param jsfe
	 * @return Returns the 'return type' of the given name.
	 */
    public static Property lookupTypeFromNameHash(String fullname, IScope scope, int offset, JSOffsetMapper jsfe) {
        synchronized (environment) {
            if (fullname.length() == 0) {
                return null;
            }
            Property result = null;
            String[] names = fullname.split("\\.");
            IObject obj = scope;
            for (int i = 0; i < names.length; i++) {
                String name = names[i];
                boolean isMethodCall = false;
                if (name.endsWith("()")) {
                    isMethodCall = true;
                    name = name.substring(0, name.length() - 2);
                }
                if (i == 0 && names.length > 1) {
                    obj = scope.getVariableValue(name, jsfe.getFileIndex(), offset).getInstance(environment, jsfe.getFileIndex(), offset);
                } else if (i < names.length - 1 && names.length > 1) {
                    obj = obj.getPropertyValue(name, jsfe.getFileIndex(), offset).getInstance(environment, jsfe.getFileIndex(), offset);
                } else {
                    result = getPropertyInScope(obj, name);
                }
                if (obj == undef) {
                    return null;
                }
                if (isMethodCall) {
                    IDocumentation doc = obj.getDocumentation();
                    if (doc instanceof FunctionDocumentation) {
                        FunctionDocumentation fdoc = (FunctionDocumentation) doc;
                        String[] rettypes = fdoc.getReturn().getTypes();
                        if (rettypes.length > 0) {
                            String rettype = rettypes[0];
                            obj = jsfe.lookupReturnTypeFromNameHash(rettype, jsfe.getGlobal());
                            if (obj != null) {
                                obj = obj.getPropertyValue("prototype", jsfe.getFileIndex(), offset);
                            }
                        }
                    } else {
                    }
                }
            }
            return result;
        }
    }

    /**
	 * getIdentName
	 * 
	 * @param position
	 * @param lexemeList
	 * @return String
	 */
    public static String getIdentName(int position, LexemeList lexemeList) {
        String name = "";
        while (position >= 0) {
            Lexeme curLexeme = lexemeList.get(position);
            switch(curLexeme.typeIndex) {
                case JSTokenTypes.IDENTIFIER:
                case JSTokenTypes.DOT:
                    name = curLexeme.getText() + name;
                    position--;
                    break;
                case JSTokenTypes.RPAREN:
                    position = -1;
                    break;
                default:
                    position = -1;
                    break;
            }
        }
        return name;
    }

    /**
	 * getPropertyInScope
	 * 
	 * @param object
	 * @param propName
	 * @return Property
	 */
    public static Property getPropertyInScope(IObject object, String propName) {
        Property result = object.getProperty(propName);
        if (result != null || !(object instanceof IScope)) {
            return result;
        }
        IScope scope = ((IScope) object).getParentScope();
        while (scope != null) {
            result = scope.getProperty(propName);
            if (result != null) {
                break;
            }
            scope = scope.getParentScope();
        }
        return result;
    }

    /**
	 * Returns the invocation mode (invoking, new, or normal).
	 * 
	 * @param offset
	 *            The index to check the mode at.
	 * @return Returns the invocation mode (invoking, new, or normal).
	 */
    public String getMode(int offset) {
        getArgIndexAndCalculateMode();
        return mode;
    }

    /**
	 * Gets the full name of of an object that is at the passed offset. This is in the form
	 * "Math.abs().toString()", even if there are arguments.
	 * 
	 * @return Returns a hashed string name
	 */
    public String getNameHash() {
        String name = NOT_AN_IDENTIFIER;
        int position = getCurrentLexemeIndex();
        int parenCount = 0;
        int bracketCount = 0;
        boolean wasSeparator = true;
        int lastTokenType = -1;
        while (position >= 0) {
            Lexeme curLexeme = getLexemeList().get(position);
            if (name.equals(NOT_AN_IDENTIFIER)) {
                if (curLexeme.typeIndex == JSTokenTypes.NEW) {
                    mode = MODE_NEW;
                    name = MODE_NEW;
                    break;
                } else if (curLexeme.typeIndex == JSTokenTypes.STRING) {
                    name = MODE_STRING;
                } else {
                    name = "";
                }
            }
            switch(curLexeme.typeIndex) {
                case JSTokenTypes.NEW:
                    mode = MODE_NEW;
                    name = MODE_NEW + name;
                    wasSeparator = true;
                    position = 0;
                    break;
                case JSTokenTypes.LPAREN:
                    wasSeparator = true;
                    if (parenCount == 0) {
                        if (position > 0) {
                            Lexeme prevLex = getLexemeList().get(position - 1);
                            if (prevLex.getCategoryIndex() == TokenCategories.KEYWORD && prevLex.typeIndex != JSTokenTypes.TYPEOF) {
                                return name;
                            } else {
                                position = 0;
                                break;
                            }
                        }
                    }
                    parenCount--;
                    name = curLexeme.getText() + name;
                    break;
                case JSTokenTypes.IDENTIFIER:
                    if (lastTokenType != JSTokenTypes.IDENTIFIER) {
                        name = curLexeme.getText() + name;
                        wasSeparator = false;
                    } else {
                        position = 0;
                    }
                    break;
                case JSTokenTypes.DOT:
                    wasSeparator = true;
                    name = curLexeme.getText() + name;
                    break;
                case JSTokenTypes.RPAREN:
                    wasSeparator = true;
                    name = ")" + name;
                    int startParenCount = parenCount;
                    parenCount++;
                    while (--position > 0) {
                        Lexeme lx = getLexemeList().get(position);
                        if (lx.typeIndex == JSTokenTypes.LPAREN) {
                            parenCount--;
                        } else if (lx.typeIndex == JSTokenTypes.RPAREN) {
                            parenCount++;
                        }
                        if (startParenCount == parenCount) {
                            name = "(" + name;
                            break;
                        }
                    }
                    break;
                case JSTokenTypes.LBRACKET:
                    wasSeparator = true;
                    if (bracketCount == 0 && position > 0) {
                        position = 0;
                        break;
                    }
                    bracketCount--;
                    name = "[" + name;
                    break;
                case JSTokenTypes.RBRACKET:
                    wasSeparator = true;
                    name = "]" + name;
                    int startBracketCount = bracketCount;
                    bracketCount++;
                    while (--position > 0) {
                        Lexeme lx = getLexemeList().get(position);
                        if (lx.typeIndex == JSTokenTypes.LBRACKET) {
                            bracketCount--;
                        } else if (lx.typeIndex == JSTokenTypes.RBRACKET) {
                            bracketCount++;
                        }
                        if (startBracketCount == bracketCount) {
                            name = "[" + name;
                            break;
                        }
                    }
                    break;
                case JSTokenTypes.WHITESPACE:
                    break;
                default:
                    if (curLexeme.getCategoryIndex() == TokenCategories.KEYWORD) {
                        if (wasSeparator) {
                            name = curLexeme.getText() + name;
                        }
                        wasSeparator = false;
                    }
                    if (position > 0) {
                        if (getLexemeList().get(position - 1).typeIndex != JSTokenTypes.DOT) {
                            position = 0;
                        }
                    } else {
                        position = 0;
                    }
                    break;
            }
            if (curLexeme.isAfterEOL()) {
                break;
            }
            lastTokenType = curLexeme.typeIndex;
            position--;
        }
        if (name.startsWith(".")) {
            name = NOT_AN_IDENTIFIER;
        }
        return name;
    }

    /**
	 * getArgAssistNameHash
	 * 
	 * @return String
	 */
    public String getArgAssistNameHash() {
        if (this.getCurrentLexeme() == null) {
            return NOT_INVOKING;
        }
        String name = NOT_INVOKING;
        int position = getCurrentLexemeIndex();
        int parenCount = 0;
        boolean wasSeparator = true;
        boolean foundSoloLParen = false;
        try {
            int curOffset = this.getCurrentLexeme().offset + 1;
            String src = this.getFileService().getSource();
            int startLine = curOffset;
            if (startLine >= src.length()) {
                startLine = src.length() - 1;
            }
            for (; startLine > 0; startLine--) {
                if (src.charAt(startLine) == '\n') {
                    startLine++;
                    break;
                }
            }
            if (startLine < 0 || curOffset > src.length() - 1) {
                return NOT_INVOKING;
            }
            if (startLine > curOffset) {
                return NOT_INVOKING;
            }
            char[] lineChars = src.substring(startLine, curOffset).toCharArray();
            int left = 0;
            int right = 0;
            for (int i = 0; i < lineChars.length; i++) {
                if (lineChars[i] == '(') {
                    left++;
                } else if (lineChars[i] == ')') {
                    right++;
                }
            }
            if (left <= right) {
                return NOT_INVOKING;
            }
        } catch (Exception e) {
            return NOT_INVOKING;
        }
        while (position >= 0) {
            Lexeme curLexeme = getLexemeList().get(position);
            if (name.equals(NOT_INVOKING)) {
                name = "";
            }
            switch(curLexeme.typeIndex) {
                case JSTokenTypes.LPAREN:
                    wasSeparator = true;
                    if (parenCount == 0) {
                        foundSoloLParen = true;
                        if (position > 0) {
                            Lexeme prevLex = getLexemeList().get(position - 1);
                            if (prevLex.getCategoryIndex() == TokenCategories.KEYWORD && prevLex.typeIndex != JSTokenTypes.TYPEOF) {
                                return NOT_INVOKING;
                            }
                        }
                    } else if (parenCount < 0) {
                        position = 0;
                        break;
                    }
                    name = curLexeme.getText() + name;
                    parenCount--;
                    break;
                case JSTokenTypes.IDENTIFIER:
                    if (foundSoloLParen) {
                        name = curLexeme.getText() + name;
                        wasSeparator = false;
                    }
                    break;
                case JSTokenTypes.COMMA:
                    if (foundSoloLParen) {
                        position = 0;
                    } else {
                        wasSeparator = true;
                        name = curLexeme.getText() + name;
                    }
                    break;
                case JSTokenTypes.DOT:
                    if (foundSoloLParen) {
                        wasSeparator = true;
                        name = curLexeme.getText() + name;
                    }
                    break;
                case JSTokenTypes.RPAREN:
                    if (foundSoloLParen) {
                        wasSeparator = true;
                        name = ")" + name;
                    }
                    int startParenCount = parenCount;
                    parenCount++;
                    while (--position > 0) {
                        Lexeme lx = getLexemeList().get(position);
                        if (lx.typeIndex == JSTokenTypes.LPAREN) {
                            parenCount--;
                        } else if (lx.typeIndex == JSTokenTypes.RPAREN) {
                            parenCount++;
                        }
                        if (startParenCount == parenCount) {
                            if (foundSoloLParen) {
                                name = "(" + name;
                            }
                            break;
                        }
                    }
                    break;
                case JSTokenTypes.WHITESPACE:
                    break;
                default:
                    if (curLexeme.getCategoryIndex() == TokenCategories.KEYWORD) {
                        if (wasSeparator && foundSoloLParen) {
                            name = curLexeme.getText() + name;
                            wasSeparator = false;
                        }
                    } else if (curLexeme.getCategoryIndex() == TokenCategories.LITERAL) {
                        break;
                    }
                    position = 0;
                    break;
            }
            if (curLexeme.isAfterEOL()) {
                break;
            }
            position--;
        }
        if (name.startsWith(".")) {
            name = NOT_INVOKING;
        }
        return name;
    }

    /**
	 * Looks up an object in the global table based on its full name. This uses the special notation
	 * (from getNameHash) for full names, and uses documentation return types and prototype/object
	 * lookup to discover return types.
	 * 
	 * @param fullname
	 *            Full name of the object to lookup
	 * @param scope
	 *            The scope to start looking from - this allows lookup from inside a function scope.
	 * @return Returns the 'return type' of the given name.
	 */
    public IObject lookupReturnTypeFromNameHash(String fullname, IScope scope) {
        return lookupReturnTypeFromNameHash(fullname, scope, false);
    }

    /**
	 * Looks up an object in the global table based on its full name. This uses the special notation
	 * (from getNameHash) for full names, and uses documentation return types and prototype/object
	 * lookup to discover return types.
	 * 
	 * @param fullname
	 *            Full name of the object to lookup
	 * @param scope
	 *            The scope to start looking from - this allows lookup from inside a function scope.
	 * @param searchForward
	 *            Looks forward in doc if part of name not found at current fileIndex/offset
	 * @return Returns the 'return type' of the given name.
	 */
    public IObject lookupReturnTypeFromNameHash(String fullname, IScope scope, boolean searchForward) {
        synchronized (this.getFileService()) {
            if (fullname.length() == 0) {
                return null;
            }
            String[] names = fullname.split("\\.");
            IObject obj = scope;
            int offset = 0;
            if (this.getCurrentLexeme() != null) {
                offset = this.getCurrentLexeme().offset;
            }
            int fileIndex = this.getFileIndex();
            for (int i = 0; i < names.length; i++) {
                String name = names[i];
                boolean isMethodCall = false;
                if (name.endsWith("()")) {
                    isMethodCall = true;
                    name = name.substring(0, name.length() - 2);
                }
                boolean isArrayCall = false;
                if (name.endsWith("[]")) {
                    isArrayCall = true;
                    name = name.substring(0, name.length() - 2);
                }
                if (i == 0) {
                    if (name.equals("this") && obj instanceof IScope) {
                        boolean hasDocReturn = false;
                        IFunction enclFn = ((IScope) obj).getEnclosingFunction();
                        if (enclFn != null && enclFn instanceof JSFunction) {
                            JSFunction fn = (JSFunction) enclFn;
                            IDocumentation doc = fn.getDocumentation();
                            if (doc instanceof PropertyDocumentation) {
                                PropertyDocumentation pdoc = (PropertyDocumentation) doc;
                                if (pdoc instanceof FunctionDocumentation && ((FunctionDocumentation) pdoc).getIsConstructor()) {
                                    obj = fn.getPropertyValue("prototype", fileIndex, offset);
                                } else {
                                    String rettype = fn.getMemberOf();
                                    if (rettype != null && !rettype.equals("")) {
                                        hasDocReturn = true;
                                        if (rettype.indexOf(".") > -1) {
                                            obj = lookupNamespaceFromNameHash(rettype);
                                        } else {
                                            obj = lookupReturnTypeFromNameHash(rettype, getGlobal());
                                        }
                                        if (obj != null) {
                                            obj = obj.getPropertyValue("prototype", fileIndex, offset);
                                        }
                                    }
                                }
                            }
                            if (!hasDocReturn) {
                                obj = fn.getGuessedMemberObject();
                            }
                        }
                    } else {
                        obj = scope.getVariableValue(name, fileIndex, offset).getInstance(environment, fileIndex, offset);
                        if ((obj == null || obj == ObjectBase.UNDEFINED) && searchForward) {
                            obj = scope.getVariableValue(name, Integer.MAX_VALUE, Integer.MAX_VALUE).getInstance(environment, Integer.MAX_VALUE, Integer.MAX_VALUE);
                        }
                    }
                } else {
                    IObject temp = obj.getPropertyValue(name, fileIndex, offset).getInstance(environment, fileIndex, offset);
                    if ((temp == null || temp == ObjectBase.UNDEFINED) && searchForward) {
                        temp = obj.getPropertyValue(name, Integer.MAX_VALUE, Integer.MAX_VALUE).getInstance(environment, Integer.MAX_VALUE, Integer.MAX_VALUE);
                    }
                    obj = temp;
                }
                if (obj == null || obj == ObjectBase.UNDEFINED) {
                    return null;
                }
                boolean isFunction = obj instanceof IFunction;
                if (isArrayCall || isMethodCall || (!isFunction && i == names.length - 1 && fullname.endsWith("."))) {
                    IDocumentation doc = obj.getDocumentation();
                    if (doc instanceof PropertyDocumentation) {
                        PropertyDocumentation pdoc = (PropertyDocumentation) doc;
                        String[] rettypes = pdoc.getReturn().getTypes();
                        if (rettypes.length > 0) {
                            String rettype = rettypes[0];
                            if (isArrayCall && rettypes.length > 1) {
                                rettype = rettypes[1];
                            }
                            if (rettype.indexOf(".") > -1) {
                                obj = lookupNamespaceFromNameHash(rettype);
                            } else {
                                obj = lookupReturnTypeFromNameHash(rettype, getGlobal());
                            }
                            if (obj != null && !name.equals("Math")) {
                                obj = obj.getPropertyValue("prototype", fileIndex, offset);
                            }
                        }
                    } else {
                        if (obj instanceof JSFunction) {
                            JSFunction fnObj = (JSFunction) obj;
                            obj = fnObj.invoke(environment, new IObject[0], fileIndex, fnObj.getRange());
                        }
                    }
                }
            }
            return obj;
        }
    }

    private IObject lookupNamespaceFromNameHash(String fullname) {
        if (fullname.length() == 0) {
            return null;
        }
        String[] names = fullname.split("\\.");
        IScope scope = getGlobal();
        IObject obj = scope;
        int offset = this.getCurrentLexeme().offset;
        int fileIndex = this.getFileIndex();
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            if (i == 0) {
                obj = scope.getVariableValue(name, fileIndex, offset).getInstance(environment, fileIndex, offset);
            } else {
                obj = obj.getPropertyValue(name, fileIndex, offset).getInstance(environment, fileIndex, offset);
            }
            if (obj == ObjectBase.UNDEFINED) {
                return null;
            }
        }
        return obj;
    }

    /**
	 * Gets the index of the arg from the current offset (arg0, arg1, ...), based on the number of
	 * commas viewed. This also sets the 'mode' the current location is in (invoking, new, normal).
	 * This does both at once toab avoid duplication, and caches the result.
	 * 
	 * @return Returns the current arg count based on commas in the source.
	 */
    public int getArgIndexAndCalculateMode() {
        int commaCount = 0;
        mode = MODE_NORMAL;
        int pos = getCurrentLexemeIndex();
        int parenCount = 0;
        boolean wasNewline = false;
        while (pos >= 0 && pos < getLexemeList().size()) {
            Lexeme curLexeme = getLexemeList().get(pos);
            if (wasNewline) {
                break;
            }
            if (curLexeme.isAfterEOL()) {
                wasNewline = true;
            }
            switch(curLexeme.typeIndex) {
                case JSTokenTypes.COMMA:
                    if (parenCount == 0) {
                        commaCount++;
                    }
                    break;
                case JSTokenTypes.RPAREN:
                    parenCount++;
                    break;
                case JSTokenTypes.DOT:
                case JSTokenTypes.WHITESPACE:
                    break;
                case JSTokenTypes.SEMICOLON:
                    mode = MODE_NORMAL;
                    pos = -1;
                    break;
                case JSTokenTypes.LPAREN:
                    parenCount--;
                    if (parenCount < 0) {
                        mode = MODE_INVOKING;
                        pos = -1;
                    }
                    break;
                default:
                    break;
            }
            pos--;
        }
        return commaCount;
    }

    /**
	 * getFileIndex
	 * 
	 * @return int
	 */
    public int getFileIndex() {
        return this.getParseState().getFileIndex();
    }

    /**
	 * Gets the global object for this file's environment.
	 * 
	 * @return Returns the global object for this file's environment.
	 */
    public JSScope getGlobal() {
        return environment.getGlobal();
    }

    /**
	 * Returns the environment scope for the specified lexeme.
	 * 
	 * @param lex
	 * @param defaultScope
	 * @return Returns the environment scope for the specified lexeme.
	 */
    public IScope getScope(Lexeme lex, IScope defaultScope) {
        return this.getEnvironmentLoader().getScope(lex.offset, defaultScope);
    }

    private LexemeBasedEnvironmentLoader getEnvironmentLoader() {
        if (loader == null) {
            loader = this.getFileLanguageService().getEnvironmentLoader();
        }
        return loader;
    }

    private JSFileLanguageService getFileLanguageService() {
        if (fileLangService == null) {
            fileLangService = JSFileLanguageService.getJSFileLanguageService(getFileService());
        }
        return fileLangService;
    }

    /**
	 * getParseState
	 * 
	 * @return IParseState
	 */
    public IParseState getParseState() {
        return this.getFileLanguageService().getParseState();
    }

    /**
	 * @see com.aptana.ide.parsing.IOffsetMapper#findTarget(com.aptana.ide.lexer.Lexeme)
	 */
    public ICodeLocation findTarget(Lexeme lexeme) {
        String fullName = this.getNameHash();
        if (fullName.indexOf('(') > -1) {
            fullName = fullName.substring(0, fullName.lastIndexOf('('));
        }
        IScope scope = this.getScope(lexeme, this.getGlobal());
        if (fullName.startsWith(JSOffsetMapper.MODE_NEW)) {
            fullName = fullName.substring(JSOffsetMapper.MODE_NEW.length());
        }
        IObject object = this.lookupReturnTypeFromNameHash(fullName, scope, true);
        Property prop = JSOffsetMapper.lookupTypeFromNameHash(fullName, scope, lexeme.getEndingOffset(), this);
        ICodeLocation loc = findTargetFromName(object, prop);
        return loc;
    }

    /**
	 * Finds a target based on the passed scope.
	 * 
	 * @param object
	 * @param prop
	 * @return Returns the target based on the passed scope.
	 */
    public static ICodeLocation findTargetFromName(IObject object, Property prop) {
        if (object == null || prop == null) {
            return null;
        }
        int offset = object.getStartingOffset();
        if (offset < 0) {
            offset = Integer.MAX_VALUE + object.getStartingOffset();
        }
        OrderedObjectCollection assignments = prop.getAssignments();
        CodeLocation loc = null;
        for (int i = 0; i < assignments.size(); i++) {
            if (assignments.get(i).object == object) {
                IWorkbenchWindow window = Workbench.getInstance().getActiveWorkbenchWindow();
                IWorkbench workbench = window.getWorkbench();
                IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
                IEditorReference[] editorReferences = page.getEditorReferences();
                for (int j = 0; j < editorReferences.length; j++) {
                    IEditorPart editor = editorReferences[j].getEditor(true);
                    IEditorSite site = editor.getEditorSite();
                    IWorkbenchPart part = site.getPart();
                    if (part instanceof UnifiedEditor) {
                        UnifiedEditor ue = (UnifiedEditor) part;
                        IFileService context = ue.getFileContext();
                        int fi = context.getParseState().getFileIndex();
                        if (assignments.get(i).fileIndex == fi) {
                            IEditorInput input = editor.getEditorInput();
                            String path = CoreUIUtils.getPathFromEditorInput(input);
                            Lexeme lx = context.getLexemeList().getCeilingLexeme(offset);
                            loc = new CodeLocation(path, lx);
                        }
                    }
                }
                if (loc != null) {
                    break;
                }
                int fileIndex = assignments.get(i).fileIndex;
                String path = FileContextManager.getURIFromFileIndex(fileIndex);
                FileService context = FileContextManager.get(path);
                if (context == null) {
                    break;
                }
                LexemeList ll = context.getLexemeList();
                if (ll == null) {
                    break;
                }
                Lexeme lx = ll.getCeilingLexeme(offset);
                loc = new CodeLocation(path, lx);
                break;
            }
        }
        return loc;
    }
}
