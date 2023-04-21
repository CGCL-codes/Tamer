package jp.ne.so_net.ga2.no_ji.jcom;

/**
 * ITypeInfo To handle interface class
 *
 * @see     IUnknown
 * @see     JComException
 * @see     ReleaseManager
	@author Yoshinori Watanabe(Yoshinori Watanabe)
	@version 2.10, 2000/07/01
	Copyright(C) Yoshinori Watanabe 1999-2000. All Rights Reserved.
 */
public class ITypeInfo extends IUnknown {

    /**
		IID_ITypeInfo �Is.
		@see       GUID
	*/
    public static GUID IID = new GUID(0x00020401, 0x0000, 0x0000, 0xC0, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x46);

    /**
     * ITypeInfo�It draws up.
	 The argument pITypeInfo appoints the pointer of  ITypeInfo interface.
     * @param     rm             Reference counter management class
     	@param	pITypeInfo	ITypeInfo Interface
     * @see       ReleaseManager
	 */
    public ITypeInfo(ReleaseManager rm, int pITypeInfo) {
        super(rm, pITypeInfo);
    }

    /**
The document of the member ID which it appoints is  returned.  
As for the member ID there is a memid of FunC$desc class.  
When the MEMBERID_NIL (-1) it appoints, the document for this object  is returned.  
As for return value with arrangement in 4 character  strings, value below each one is housed.  The inside of the  parenthesis is value in case of the Excel.Application.  
Those which do not have value have become the null.
		<pre>
		Return value[0]	bstrName        �This name.("_Application")
		Return valuel[1]	btrDocString    Document(null)
		Return valuel[2]	dwHelpContext    Changed number of help context into the character string those("131073")
		Return valuel[3]	bstrHelpFile	Full pass of help("D:\Office97\Office\VBAXL8.HLP")
		<pre>
	*/
    public String[] getDocumentation(int memid) throws JComException {
        return _getDocumentation(memid);
    }

    public static final int MEMBERID_NIL = -1;

    /**
		TypeAttr�It returns.
		TypeAttr�It is the class which manages the attribute of the  ITypeInfo.
		@see	ITypeInfo.TypeAttr
	*/
    public TypeAttr getTypeAttr() throws JComException {
        return _getTypeAttr();
    }

    /**
		The FunC$desc of the index which it appoints is  returned.
		FuncDesc�It is the class which manages the information of method.
		@see	ITypeInfo.FuncDesc
	*/
    public FuncDesc getFuncDesc(int index) throws JComException {
        return _getFuncDesc(index);
    }

    /**
		The VarDesc of the index which it appoints is returned.
		VarDesc�It is the class which manages the information of  variable (usually Enum type).
		@see	ITypeInfo.VarDesc
	*/
    public VarDesc getVarDesc(int index) throws JComException {
        return _getVarDesc(index);
    }

    /**
		The ITypeLib to which this object is generic is  returned.
		@see	ITypeLib
	*/
    public ITypeLib getTypeLib() throws JComException {
        return new ITypeLib(rm, _getTypeLib());
    }

    /**
		When this class is the COCLASS, the type information  which is been mounted really is returned.
	*/
    public ITypeInfo getImplType(int index) throws JComException {
        return new ITypeInfo(rm, _getImplType(index));
    }

    /**
		The ITypeInfo which is appointed with the hreftype is  returned.
		@see ITypeInfo.ElemDesc#getTypeDesc()
	*/
    public ITypeInfo getRefTypeInfo(int hreftype) throws JComException {
        return new ITypeInfo(rm, _getRefTypeInfo(hreftype));
    }

    /**
		When type information is the VT_USERDEFINED, the  HREFTYPE is removed from there.
		HREFTYPE�It is displayed at hexadecimal number.
	*/
    public static int getRefTypeFromTypeDesc(String type) {
        return Integer.parseInt(type.substring(type.indexOf('(') + 1, type.indexOf(')')), 16);
    }

    /**
		It shows whether or not this object and other object are equal.
		ITypeInfo.TypeAttr.IID With whether or not identical type information it has  judged
		@see ITypeInfo.TypeAttr#getIID()
	*/
    public boolean equals(Object obj) {
        if (!(obj instanceof ITypeInfo)) return false;
        ITypeInfo info = (ITypeInfo) obj;
        try {
            return info.getTypeAttr().getIID().equals(this.getTypeAttr().getIID());
        } catch (JComException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
		ITypeInfo�It is the class which manages attribute.
		GUID The number of functions and the number of variables are  returned.
		Variable in constant, differs from the property usually. 
                             Setting and acquisition of the property are included in function.
		@see	ITypeInfo
	*/
    public class TypeAttr {

        public static final int TKIND_ENUM = 0;

        public static final int TKIND_RECORD = 1;

        public static final int TKIND_MODULE = 2;

        public static final int TKIND_INTERFACE = 3;

        public static final int TKIND_DISPATCH = 4;

        public static final int TKIND_COCLASS = 5;

        public static final int TKIND_ALIAS = 6;

        public static final int TKIND_UNION = 7;

        public static final int TKIND_MAX = 8;

        private GUID IID;

        private int cFuncs;

        private int cVars;

        private int cImplTypes;

        private int typekind;

        public TypeAttr(GUID IID, int typekind, int cFuncs, int cVars, int cImplTypes) {
            this.IID = IID;
            this.typekind = typekind;
            this.cFuncs = cFuncs;
            this.cVars = cVars;
            this.cImplTypes = cImplTypes;
        }

        public GUID getIID() {
            return IID;
        }

        public int getTypeKind() {
            return typekind;
        }

        public int getFuncs() {
            return cFuncs;
        }

        public int getVars() {
            return cVars;
        }

        public int getImplTypes() {
            return cImplTypes;
        }
    }

    /**
                It has the information of type such as one argument and  return value.  
                It is the class which displays the ELEMDESC structure  of the COM.  
                [ It has the information of the IN out ] and the like and the information of 
                the type VT_PTR+VT_BSTR and the like.
                When it has the VT_USERDEFINED, you acquire the class  name automatically, 
                replace to the actual character string.  
                You keep  type in the form in the character string, become Java interchangeable  shape. 
                Namely, as for the VT_PTR|VT_I4 as for " [ I " and the  VT_UNKNOWN 
                it becomes " the Ljp.ne.so_net.ga2.no_ji.jcom.IUnknown ".
	  @see	ITypeInfo.FuncDesc
	*/
    public class ElemDesc {

        public static final int IDLFLAG_FIN = 0x01;

        public static final int IDLFLAG_FOUT = 0x02;

        public static final int IDLFLAG_FLCID = 0x04;

        public static final int IDLFLAG_FRETVAL = 0x08;

        private int idl;

        private String typedesc;

        public ElemDesc(String typedesc, int idl) {
            this.typedesc = typedesc;
            this.idl = idl;
        }

        public int getIDL() {
            return idl;
        }

        /**
			�^����Ԃ��܂��B�ȉ��̌`���ł��B
			"VT_I4" "VT_BSTR" "VT_DISPATCH" "VT_PTR+VT_I4"
			"VT_SAFEARRAY+VT_I4"
			"VT_USERDEFINED(1):VBE"
			VT_USERDEFINED�̊��ʓ��̐��l��16�i���ŕ\������hreftype�ŁA���̐��l��
			getRefTypeInfo()�ɓn�����Ƃɂ��A���̒l��ITypeInfo��
			�擾���邱�Ƃ��ł��܂��B
		*/
        public String getTypeDesc() {
            return typedesc;
        }

        public String toString() {
            if (idl == 0) return typedesc;
            String result = "";
            if ((idl & IDLFLAG_FIN) != 0) result += "[in]";
            if ((idl & IDLFLAG_FOUT) != 0) result += "[out]";
            if ((idl & IDLFLAG_FLCID) != 0) result += "[lcid]";
            if ((idl & IDLFLAG_FRETVAL) != 0) result += "[retval]";
            return result + typedesc;
        }
    }

    /**
		���\�b�h�̏����Ǘ�����N���X�ł��B
		�����o�h�c�A����ьĂяo���`���A��̌^��߂�l�̌^�Ȃǂ�
		�����܂��B
		@see	ITypeInfo.ElemDesc
		@see	ITypeInfo
	*/
    public class FuncDesc {

        private int memid;

        private int invkind;

        private ElemDesc[] elemdescParam;

        private ElemDesc elemdescFunc;

        public static final int INVOKE_FUNC = 0x01;

        public static final int INVOKE_PROPERTYGET = 0x02;

        public static final int INVOKE_PROPERTYPUT = 0x04;

        public static final int INVOKE_PROPERTYPUTREF = 0x08;

        /**
			���\�b�h�̏��𐶐����܂��B
			ITypeInfo.getFuncDesc()���Ŏg�p����܂��B
			�ʏ�A�O������͎g�p���܂���B
			@see	ITypeInfo#getFuncDesc(int)
		*/
        public FuncDesc(int memid, int invkind, ElemDesc[] elemdescParam, ElemDesc elemdescFunc) {
            this.memid = memid;
            this.invkind = invkind;
            this.elemdescParam = elemdescParam;
            this.elemdescFunc = elemdescFunc;
        }

        /**
			���\�b�h�̏���\�����܂��B
		*/
        public String toString() {
            try {
                String[] names = getNames();
                String result = "";
                switch(invkind) {
                    case INVOKE_FUNC:
                        result += "FUNC ";
                        break;
                    case INVOKE_PROPERTYGET:
                        result += "GET ";
                        break;
                    case INVOKE_PROPERTYPUT:
                        result += "PUT ";
                        break;
                    case INVOKE_PROPERTYPUTREF:
                        result += "PUTREF ";
                        break;
                }
                result += names[0] + "(";
                if (elemdescParam != null) {
                    for (int i = 0; i < elemdescParam.length; i++) {
                        result += elemdescParam[i].toString() + " ";
                        if (i + 1 < names.length) result += names[i + 1];
                        if (i != elemdescParam.length - 1) result += ",";
                    }
                }
                result += ")" + elemdescFunc.toString();
                return result;
            } catch (Exception e) {
            }
            return null;
        }

        /**
			�����o�h�c��Ԃ��܂��B0�ȏ�̒l�ł��B
		*/
        public int getMemID() {
            return memid;
        }

        /**
			�Ăяo���`����Ԃ��܂��B
			INVOKE_XXX�̂����ꂩ�ł��B
			@see	ITypeInfo.FuncDesc#INVOKE_FUNC
			@see	ITypeInfo.FuncDesc#INVOKE_PROPERTYGET
			@see	ITypeInfo.FuncDesc#INVOKE_PROPERTYPUT
			@see	ITypeInfo.FuncDesc#INVOKE_PROPERTYPUTREF
		*/
        public int getInvokeKind() {
            return invkind;
        }

        /**
			��̏���Ԃ��܂��B
			���Ȃ��ꍇ��<code>null</code>��Ԃ��܂��B
			@see	ITypeInfo.ElemDesc
		*/
        public ElemDesc[] getParams() {
            return elemdescParam;
        }

        /**
			�߂�l�̏���Ԃ��܂��B
			@see	ITypeInfo.ElemDesc
		*/
        public ElemDesc getFunc() {
            return elemdescFunc;
        }

        /**
			���\�b�h�̖��O�A��̖��O��Ԃ��܂��B
			�ǂ����A[0]�����\�b�h���A[1]�ȍ~����̖��O�̂悤�ł��B
		*/
        public String[] getNames() throws JComException {
            return _getNames(memid);
        }
    }

    /**
		�����ϐ��̏����Ǘ�����N���X�ł��B
		�ʏ�AEnum�^�̒萔�Ɏg���܂��B
		@see	ITypeInfo
		@see	ITypeInfo.ElemDesc
	*/
    public class VarDesc {

        private int memid;

        private int varkind;

        private ElemDesc elemdescVar;

        private Object varValue;

        public static final int VAR_PERINSTANCE = 0;

        public static final int VAR_STATIC = 1;

        public static final int VAR_CONST = 2;

        public static final int VAR_DISPATCH = 3;

        /**
			���\�b�h�̏��𐶐����܂��B
			ITypeInfo.getFuncDesc()���Ŏg�p����܂��B
			�ʏ�A�O������͎g�p���܂���B
			@see	ITypeInfo#getFuncDesc(int)
		*/
        public VarDesc(int memid, int varkind, ElemDesc elemdescVar, Object varValue) {
            this.memid = memid;
            this.varkind = varkind;
            this.elemdescVar = elemdescVar;
            this.varValue = varValue;
        }

        /**
			���\�b�h�̏���\�����܂��B
		*/
        public String toString() {
            try {
                String[] names = getNames();
                String result = "";
                switch(varkind) {
                    case VAR_PERINSTANCE:
                        result += "PERINSTANCE ";
                        break;
                    case VAR_STATIC:
                        result += "STATIC ";
                        break;
                    case VAR_CONST:
                        result += "CONST ";
                        break;
                    case VAR_DISPATCH:
                        result += "DISPATCH ";
                        break;
                }
                result += elemdescVar.toString() + " " + names[0] + " = " + varValue.toString();
                return result;
            } catch (Exception e) {
            }
            return null;
        }

        /**
			�����o�h�c��Ԃ��܂��B0�ȏ�̒l�ł��B
		*/
        public int getMemID() {
            return memid;
        }

        /**
			�ϐ��̌`����Ԃ��܂��B
			VAR_XXX�̂����ꂩ�ł��B
			@see	ITypeInfo.VarDesc#VAR_PERINSTANCE
			@see	ITypeInfo.VarDesc#VAR_STATIC
			@see	ITypeInfo.VarDesc#VAR_CONST
			@see	ITypeInfo.VarDesc#VAR_DISPATCH
		*/
        public int getVarKind() {
            return varkind;
        }

        /**
			�ϐ��̌^�̏���Ԃ��܂��B
			@see	ITypeInfo.ElemDesc
		*/
        public ElemDesc getVar() {
            return elemdescVar;
        }

        /**
			���\�b�h�̖��O�A��̖��O��Ԃ��܂��B
			�ǂ����A[0]�����\�b�h���A[1]�ȍ~����̖��O�̂悤�ł��B
		*/
        public String[] getNames() throws JComException {
            return _getNames(memid);
        }

        /**
			�ϐ��̒l��Ԃ��܂��B
		*/
        public Object getValue() {
            return varValue;
        }
    }

    private native String[] _getDocumentation(int memid) throws JComException;

    private native String[] _getNames(int memid) throws JComException;

    private native TypeAttr _getTypeAttr() throws JComException;

    private native FuncDesc _getFuncDesc(int index) throws JComException;

    private native VarDesc _getVarDesc(int index) throws JComException;

    private native int _getImplType(int index) throws JComException;

    private native int _getTypeLib() throws JComException;

    private native int _getRefTypeInfo(int hreftype) throws JComException;
}
