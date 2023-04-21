package net.sf.opentranquera.providers.taglibs.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.taglib.html.FormTag;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;
import net.sf.opentranquera.providers.taglibs.comboselect.ComboSelectTag;
import net.sf.opentranquera.providers.taglibs.comboselect.Ddl;

/**
 * @author wzrzjb
 * Extiende el Select TAG de struts para agregarle el 
 * comportamiento de keyDown asi va par�ndose en el item
 * a medida que se escribe en el teclado.
 */
public class SelectTag extends org.apache.struts.taglib.html.SelectTag {

    private String doMatches = null;

    /**
	 * Agrega el JavaScript para manejar el combo en keyDown.
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
    public int doStartTag() throws JspException {
        StringBuffer sb = new StringBuffer();
        Object pusoScript = ((HttpServletRequest) pageContext.getRequest()).getAttribute(this.getClass().getName());
        if (pusoScript == null) {
            ((HttpServletRequest) pageContext.getRequest()).setAttribute(this.getClass().getName(), "Y");
            sb.append(this.getScript());
        }
        sb.append("<SCRIPT LANGUAGE=\"JavaScript\">\n");
        sb.append("idBuffer = bufferManager.addBuffer();\n");
        sb.append("</SCRIPT>\n");
        ResponseUtils.write(pageContext, sb.toString());
        String newKeyDown = "";
        if (this.getOnkeydown() != null) newKeyDown = this.getOnkeydown();
        if ("true".equalsIgnoreCase(this.getDoMatches())) {
            newKeyDown += ";return findListItem(idBuffer);";
            this.setOnkeydown(newKeyDown);
        }
        int ret = super.doStartTag();
        this.manageComboSelect();
        return ret;
    }

    protected void manageComboSelect() throws JspException {
        ComboSelectTag parent = (ComboSelectTag) findAncestorWithClass(this, ComboSelectTag.class);
        if (parent != null) {
            String valor = null;
            if (value != null) {
                valor = ResponseUtils.filter(value);
            } else {
                Object valueAux = RequestUtils.lookup(pageContext, name, property, null);
                if (valueAux == null) valueAux = "";
                valor = ResponseUtils.filter(valueAux.toString());
            }
            String selectname = "document.forms[\"" + this.getFormName() + "\"].elements[\"" + this.getProperty() + "\"]";
            parent.addDdl(new Ddl(selectname, valor, this.getOnchange(), this.getProperty()));
        }
    }

    protected String getScript() {
        StringBuffer sb = new StringBuffer();
        sb.append("<script>\n<!--\n");
        sb.append("	//************************************************************\n");
        sb.append("	//CONSTANTES - Para regular el time out\n");
        sb.append("	//************************************************************\n");
        sb.append("	var BUFFER_TIMEOUT = 12;\n");
        sb.append("	var REFRESH_DELAY = 100;\n");
        sb.append("	//************************************************************\n");
        sb.append("	//OBJETOS DE BUFFER Y BUFFERMANAGER\n");
        sb.append("	//************************************************************\n");
        sb.append("	function Buffer() {\n");
        sb.append("		this.str = '';\n");
        sb.append("		this.timeOut = 0;\n");
        sb.append("	}\n");
        sb.append("	function BufferManager() {\n");
        sb.append("		this.addBuffer=addBuffer;\n");
        sb.append("		this.incrementTimeOut=incrementTimeOut;\n");
        sb.append("		this.resetBuffer=resetBuffer;\n");
        sb.append("		this.buffers=new Array();\n");
        sb.append("		this.getBufferLength=getBufferLength;\n");
        sb.append("		this.appendToBuffer=appendToBuffer;\n");
        sb.append("		this.getBufferString=getBufferString;\n");
        sb.append("		this.getBufferTimeOut=getBufferTimeOut;\n");
        sb.append("		this.getBuffersCount=getBuffersCount;\n");
        sb.append("		this.resetTimeOut=resetTimeOut;\n");
        sb.append("	}\n");
        sb.append("	function addBuffer() {\n");
        sb.append("		this.buffers[this.buffers.length] = new Buffer();\n");
        sb.append("		return this.buffers.length - 1;\n");
        sb.append("	}\n");
        sb.append("	function incrementTimeOut(bufferId) {\n");
        sb.append("		this.buffers[bufferId].timeOut++;\n");
        sb.append("	}\n");
        sb.append("	function resetTimeOut(bufferId) {\n");
        sb.append("		this.buffers[bufferId].timeOut=0;\n");
        sb.append("	}\n");
        sb.append("	function resetBuffer(bufferId) {\n");
        sb.append("		this.buffers[bufferId].timeOut=0;\n");
        sb.append("		this.buffers[bufferId].str='';\n");
        sb.append("	}\n");
        sb.append("	function getBufferLength(bufferId) {\n");
        sb.append("		return this.buffers[bufferId].str.length;\n");
        sb.append("	}\n");
        sb.append("	function appendToBuffer(bufferId, chr) {\n");
        sb.append("		this.buffers[bufferId].str+=chr;\n");
        sb.append("	}\n");
        sb.append("	function getBufferString(bufferId) {\n");
        sb.append("		return this.buffers[bufferId].str;\n");
        sb.append("	}\n");
        sb.append("	function getBufferTimeOut(bufferId) {\n");
        sb.append("		return this.buffers[bufferId].timeOut;\n");
        sb.append("	}\n");
        sb.append("	function getBuffersCount() {\n");
        sb.append("		return this.buffers.length;\n");
        sb.append("	}\n");
        sb.append("	var bufferManager = new BufferManager();\n");
        sb.append("	//************************************************************\n");
        sb.append("	//FUNCION DE MANEJO DE LAS LISTAS\n");
        sb.append("	//************************************************************\n");
        sb.append("	function findListItem(bufferId) {\n");
        sb.append("		var plstObject = event.srcElement;\n");
        sb.append("		if ( ( event.keyCode > 64 && event.keyCode < 91 ) || ( event.keyCode > 47 && event.keyCode < 58 ) || event.keyCode == 32 ) {\n");
        sb.append("			bufferManager.appendToBuffer(bufferId, String.fromCharCode( event.keyCode ));\n");
        sb.append("			var pintLen = bufferManager.getBufferLength(bufferId);\n");
        sb.append("			for( var pintIdx = 0; pintIdx < plstObject.length; pintIdx++ )\n");
        sb.append("			{\n");
        sb.append("				if ( plstObject.options[ pintIdx ].text.substring( 0, pintLen ).toLowerCase() == bufferManager.getBufferString(bufferId).toLowerCase() )\n");
        sb.append("				{\n");
        sb.append("					plstObject.selectedIndex = pintIdx;\n");
        sb.append("					break;\n");
        sb.append("				}\n");
        sb.append("			}\n");
        sb.append("			bufferManager.resetTimeOut(bufferId);\n");
        sb.append("			if(plstObject.onchange) plstObject.onchange();\nreturn false;\n");
        sb.append("		}\n");
        sb.append("		return true;\n");
        sb.append("	}\n");
        sb.append("	//************************************************************\n");
        sb.append("	// REFRESH DE LOS BUFFERS\n");
        sb.append("	//************************************************************\n");
        sb.append("	function refreshBuffers() {\n");
        sb.append("		for(var pintIdBuff = 0; pintIdBuff<bufferManager.getBuffersCount();pintIdBuff++) {\n");
        sb.append("			bufferManager.incrementTimeOut(pintIdBuff);\n");
        sb.append("			if(bufferManager.getBufferTimeOut(pintIdBuff)==BUFFER_TIMEOUT) {\n");
        sb.append("				bufferManager.resetBuffer(pintIdBuff);\n");
        sb.append("			}\n");
        sb.append("		}\n");
        sb.append("		setTimeout('refreshBuffers()', REFRESH_DELAY);\n");
        sb.append("	}\n");
        sb.append("	//inicia la refrescada\n");
        sb.append("	setTimeout('refreshBuffers()', REFRESH_DELAY);\n");
        sb.append("//-->\n</script>\n");
        return sb.toString();
    }

    protected String getFormName() {
        FormTag ft = (FormTag) pageContext.getAttribute(Constants.FORM_KEY, PageContext.REQUEST_SCOPE);
        if (ft != null) {
            return ft.getBeanName();
        } else {
            String formName = ((ActionMapping) this.pageContext.getRequest().getAttribute(Globals.MAPPING_KEY)).getName();
            return formName;
        }
    }

    /**
	 * @return
	 */
    public String getDoMatches() {
        return doMatches;
    }

    /**
	 * @param string
	 */
    public void setDoMatches(String string) {
        doMatches = string;
    }
}
