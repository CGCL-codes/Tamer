package com.webmotix.form;

import java.util.Locale;
import java.util.Map;
import javax.jcr.Session;
import com.webmotix.dao.FieldDAO;
import com.webmotix.dao.FormDAO;

/**
 * Interface padr�o para defini��o de um gerador de c�digo para o formul�rio.
 * 
 * @author wsouza
 * 
 */
public interface FormHtml {

    /**
	 * Adiciona a lista e retorna o html do campo.
	 * 
	 * @param field
	 * @return
	 */
    public String getFieldHtml(FieldDAO field);

    /**
	 * Retorna o html completo do formul�rio.
	 * 
	 * @return
	 */
    public String getHtml();

    /**
	 * Retorna o javascript caso necess�rio para processamento e valida��o do
	 * campo.
	 * 
	 * @return
	 */
    public String getJavaScript();

    public FormDAO getForm();

    public void setForm(FormDAO form);

    public Locale getLocale();

    public void setLocale(Locale locale);

    public String getId();

    public void setId(String id);

    public String getValidFunction();

    public Session getSession();

    public void setSession(Session session);

    public Map<String, Object> getValues();

    public void setValues(Map<String, Object> values);

    public Object getValue(final String name);

    public String getUuid();

    public void setUuid(String uuid);
}
