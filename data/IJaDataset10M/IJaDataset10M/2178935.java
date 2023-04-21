package org.xpmtl.mock;

import java.lang.reflect.*;
import org.xpmtl.util.*;
import org.jdom.output.*;
import org.jdom.*;
//import org.jdom.transform.*;
//import javax.xml.transform.*;
//import javax.xml.transform.stream.*;
import java.util.*;
import java.io.*;

public class XMLObjectSerializer {

	public static boolean DEBUG = false;   

	private Class m_MyClass = null;
	private Hashtable m_ListeMethode = new Hashtable();

	public XMLObjectSerializer(){
	} 

	public XMLObjectSerializer(Class cls){
		cls = m_MyClass;
	}

	public void setClass(Class cls){
		m_MyClass = cls;
		m_ListeMethode.clear();
	}

	public Document getAsDocument(){
		return new Document(getAsElement());
	}

	public String getPackage(String clsName){
		String res = "";
		try {
			res =clsName.substring(0,clsName.lastIndexOf(".")); 
		} finally {
			return res;
		}
	}

	public String getName(String clsName){
		String res = clsName;
		try {
			res =clsName.substring(clsName.lastIndexOf(".")+1,clsName.length()); 
		} finally {
			return res;
		}
	}


	public Element getAsElement() {
		Element elem = null;
		try {
			Debug.showDebug(DEBUG,"inside");
			if (m_MyClass.isInterface()) {
				elem = new Element(XMLConst.INTERFACE_TAG);
			} else {
				elem = new Element(XMLConst.CLASS_TAG);
			}
			Debug.showDebug(DEBUG,"MockGen:getAsElement m_MyClass.getName() " + m_MyClass.getName());
			elem.setAttribute(XMLConst.NAME_TAG,getName(m_MyClass.getName()));                        
			Debug.showDebug(DEBUG,"MockGen:getAsElement before adding getPackage: " + getPackage(m_MyClass.getName()));
			elem.setAttribute(XMLConst.PACKAGE_TAG,getPackage(m_MyClass.getName()));                        
			Debug.showDebug(DEBUG,"MockGen:getAsElement after adding getPackage");
			Element meths[] = getMethods(m_MyClass);
			Debug.showDebug(DEBUG," NElement meths: " + meths.length);
			for (int ind =0; ind < meths.length; ind++) {
				Debug.showDebug(DEBUG,"meths["+ ind +"]: " + meths[ind] );                       
				Debug.showDebug(DEBUG,"meths["+ ind +"]: " + meths[ind].getAttribute(XMLConst.NAME_TAG) );                       
				elem.addContent(meths[ind]);
				Debug.showDebug(DEBUG,"meths["+ ind +"] added. ");                       

			}                
			Debug.showDebug(DEBUG,"exiting elem: " + (new XMLOutputter()).outputString(elem));
		} catch (Exception e) {
			e.printStackTrace();
			Debug.showException("Exception e: " + e);
			//throw e;
		} finally {
			return elem;
		}                                                                                                                   
	}

	public String getAsXMLString(){
		String str = (new XMLOutputter()).outputString(getAsElement());
		Debug.showDebug(DEBUG,"inside str:" + str);                                                                                                                    
		return str;
	}

	public String getMethodsAsXMLString(){
		XMLOutputter out = new XMLOutputter();               
		Element meths[] = getMethods(m_MyClass);
		String res = "";
		for (int ind =0; ind < meths.length; ind++) {
			res += out.outputString(meths[ind]);                                       
		}                
		return res;                                                                
	}      


	private Element[] getMethods(Class cls){
		Element[] res;
		Element[] tmp;
//		Method[] all = cls.getDeclaredMethods();
		Method[] all = getDeclaredMethodsOrderedByName(cls);
		tmp = new Element[all.length];
		Debug.showDebug(DEBUG,"Nb Methods: " + all.length );
		int nb = 0;
		for (int ind=0; ind < all.length; ind++) {
			Element elem = getMethod(all[ind]);
			if (elem!=null) {
				tmp[nb] = elem;
				nb++;
				Debug.showDebug(DEBUG,"elem nb:"+ nb +", " + elem.getAttribute(XMLConst.NAME_TAG) );                       
			} else {
				Debug.showDebug(DEBUG,"Element for method: " + all[ind].getName() + " is null" );
			}
		}
		res = new Element[nb];
		System.arraycopy(tmp,0,res,0,nb);


		return res;
	}

	private Method[] getDeclaredMethodsOrderedByName(Class cls){
	    Method[] res = null;
	    Method[] all = cls.getDeclaredMethods();
	    Hashtable hash = new Hashtable();
	    for (int ind=0; ind <all.length; ind++) {
		hash.put(all[ind].toString(),all[ind]);
	    }
	    res = new Method[hash.size()];
	    Enumeration enum = hash.elements();
	    int ind=0;
	    while (enum.hasMoreElements()) {
		Method m = (Method)enum.nextElement();
		res[ind++]=m;
	    }


	    return res;
	}

//	private Element


	private String getType(String type){
		Debug.showDebug(DEBUG,"type: " + type);
		String res = type;
		if (type.equals("[B")) res = "byte[]";
		else
			if (type.equals("[I")) res = "int[]";
		else
			if (type.equals("[Z")) res = "boolean[]";
		else
			if (type.equals("[F")) res = "float[]";
		else
			if (type.equals("[D")) res = "double[]";
		else
			if (type.equals("[J")) res = "long[]";
		else
			if (type.equals("[S")) res = "short[]";
		else
			if (type.equals("[C")) res = "char[]";
		else
			if (type.indexOf("[")!=-1) {
			res = type.substring(2,type.length()-1);
			res += "[]";
		}
		return res;
	}

	private String getMethodIndex(String methodeName){
		int nb = 0;
		String res = "";
		if (m_ListeMethode.containsKey(methodeName)) {
			nb = ((Integer)m_ListeMethode.get(methodeName)).intValue();
			nb++;
		}
		res = "" + nb;
		m_ListeMethode.put(methodeName,new Integer(nb));             
		return res;
	}

	private String upperFirstLetter(String s){
		String tmp = "";
		tmp = s.charAt(0)+"";
		tmp = tmp.toUpperCase() + s.substring(1);
		return tmp;
	}

	private String getNameFromFullClassName(String s){
		return s.substring(s.lastIndexOf(".")+1,s.length());

	}


	private Element getMethod(Method meth){
		Element res = null;
		if ( (Modifier.isPublic( meth.getModifiers())==true) && (Modifier.isFinal( meth.getModifiers()) ==false) ) {
			res= new Element(XMLConst.METHOD_TAG);
			res.setAttribute(XMLConst.NAME_TAG,meth.getName());
			res.setAttribute(XMLConst.NORMALIZED_NAME_TAG,upperFirstLetter(meth.getName()));
			Debug.showDebug(DEBUG,meth.getName());
			res.setAttribute(XMLConst.INDEX_TAG,getMethodIndex(meth.getName())); //index des methodes

			//Exceptions
			Class[] excep = meth.getExceptionTypes();
			Element exps = new Element(XMLConst.EXCEPTIONS_TAG);
			for (int ind=0; ind < excep.length; ind++) {
				Element exp = new Element(XMLConst.EXCEPTION_TAG);
				exp.setAttribute(XMLConst.TYPE_TAG,excep[ind].getName());
				exp.setAttribute(XMLConst.NAME_TAG,getNameFromFullClassName(excep[ind].getName()));
				if ((ind+1)==excep.length)
					exp.setAttribute(XMLConst.SEPARATOR_TAG,"");
				else
					exp.setAttribute(XMLConst.SEPARATOR_TAG,",");
				//exp.addContent(excep[ind].getName());                                
				exps.addContent(exp);                        
			}
			res.addContent(exps);                        
			//ReturnType
			Element resultat = new Element(XMLConst.RESULT_TAG);
			if (meth.getReturnType()!=null) {
				resultat.addContent(getType(meth.getReturnType().getName()));
			}
			res.addContent(resultat);
			//Parameters
			Element param = new Element(XMLConst.PARAMS_TAG);
			Class[] params = meth.getParameterTypes();
			Element buf;
			if (params!=null) {
				for (int ind=0; ind < params.length ; ind++) {
					buf =new Element(XMLConst.PARAM_TAG);
					buf.setAttribute(XMLConst.TYPE_TAG,getType(params[ind].getName()));
					buf.setAttribute(XMLConst.NAME_TAG,"param" + (ind+1));
					if ((ind+1)==params.length)
						buf.setAttribute(XMLConst.SEPARATOR_TAG,"");
					else
						buf.setAttribute(XMLConst.SEPARATOR_TAG,",");                                                                        
					param.addContent(buf);
				}
			}
			res.addContent(param);
		} else {
			Debug.showDebug(DEBUG, "SimpleMockGen:getMethod " + meth.getName() + " is not public or is final.");
		}
		return res;

	}

	/*
	getDeclaredConstructors
	*/                
}
