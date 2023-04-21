package br.ufal.npd.fox.mapping;

import java.util.Collection;

public interface FoxClassMappingIF {

    public abstract void addFoxFieldMapping(FoxFieldMappingIF fieldMapping);

    /**
     * @return  Retorna o caminho completo e o nome classe (ClassPath).
     * @uml.property  name="classPath"
     */
    public abstract String getClassPath();

    /**
     * @return  Retorna o nome do idioma de indexa��o.
     * @uml.property  name="foxLanguage"
     */
    public abstract String getFoxLanguage();

    /**
     * @param language  - Nome do idioma de indexa��o.
     * @uml.property  name="foxLanguage"
     */
    public abstract void setFoxLanguage(String language);

    /**
     * @param classPath  - Caminho completo e nome da classe (classPath).
     * @uml.property  name="classPath"
     */
    public abstract void setClassPath(String classPath);

    /**
     * @return  Retorna uma cole��o de Objetos FieldMapping
     * @uml.property  name="fieldsMapping"
     */
    public abstract Collection<FoxFieldMappingIF> getFieldsMapping();

    /**
     * @return Retorna o campo chave da classe.
     */
    public abstract FoxFieldMappingIF getKeyFieldMapping();
}
