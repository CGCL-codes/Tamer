package br.ufal.npd.fox.converter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.search.Hits;
import br.ufal.npd.fox.commons.FoxObjectCreator;
import br.ufal.npd.fox.commons.StringUtil;
import br.ufal.npd.fox.commons.lang.FoxDocumentException;
import br.ufal.npd.fox.commons.lang.FoxLuceneConverterException;
import br.ufal.npd.fox.commons.lang.FoxLuceneDateConverterException;
import br.ufal.npd.fox.commons.lang.FoxMappingException;
import br.ufal.npd.fox.commons.lang.FoxObjectCreaterException;
import br.ufal.npd.fox.commons.lang.FoxResultFieldException;
import br.ufal.npd.fox.document.FoxDocumentFactory;
import br.ufal.npd.fox.document.FoxDocumentIF;
import br.ufal.npd.fox.indexersearcher.FoxIndexerSearcherIF;
import br.ufal.npd.fox.indextype.FoxIndexTypeFactory;
import br.ufal.npd.fox.indextype.FoxIndexTypeIF;
import br.ufal.npd.fox.mapping.FoxClassMappingIF;
import br.ufal.npd.fox.mapping.FoxFieldMappingIF;
import br.ufal.npd.fox.mapping.FoxMappingIF;
import br.ufal.npd.fox.result.FoxResultFactory;
import br.ufal.npd.fox.result.FoxResultFieldIF;
import br.ufal.npd.fox.result.FoxResultIF;
import br.ufal.npd.fox.result.lucene.FoxLuceneResultField;

/**
 * Classe respons�vel por converter objetos index�veis em documentos do lucene e
 * Hits do lucene em objetos que implementam FoxResultIF
 * 
 * @author �dle M�rcio (edlemarcio@gmail.com) 31/07/2006
 * @version 1.0.0 Window - Preferences - Java - Code Style - Code Templates
 * 
 * <br>
 * <br>
 * 01/08/2006 BUG FIX: - N�o estava adicionando o conte�do dos documentos ao
 * contents (campo geral) <br>
 * <br>
 * 16/01/2008 Altera��o - Movedo as constates para a
 * <code>FoxIndexerSearcherIF</code> <br> - Alterado o m�todo
 * "converterHitsToFoxResult(Hits hits, FoxMappingIF foxMapping, String
 * textSearch, String fieldSearchName)" para suportar retorno de classes de
 * resultado mapeadas no arquivo de configura��o.<br>
 * <br>
 * 21/01/2008 Altera��o - Altera para n�o imprimir o objeto e sim o nome da
 * classe<br>
 * <br>
 * 24/01/2008 BUG FIX - N�o estava verificando se a classe do objeto a ser
 * convertido existia no mapeamento
 */
public class FoxLuceneConverter {

    static Logger log = Logger.getLogger(FoxLuceneConverter.class);

    /**
     * Construtor padr�o da classe.
     */
    public FoxLuceneConverter() {
        super();
    }

    /**
     * @param document
     * @param fieldMapping
     * @param fieldValue
     * @param nullable
     * @return String com o conte�do indexado
     * @throws FoxLuceneConverterException
     */
    private String addFieldDateToDocument(Document document, FoxFieldMappingIF foxFieldMapping, Object fieldValue) throws FoxLuceneConverterException {
        FoxLuceneDateConverter convertDate = new FoxLuceneDateConverter();
        try {
            String value = convertDate.convertDateToLuceneDate(convertDate.convertDateToString((Date) fieldValue, "d/m/Y"), FoxLuceneDateConverter.DEFAULT_FOX_DATA_FORMAT);
            document.add(new Field(foxFieldMapping.getName(), value, Field.Store.YES, Field.Index.UN_TOKENIZED));
            return value;
        } catch (FoxLuceneDateConverterException e) {
            throw new FoxLuceneConverterException(e.getMessage(), e.getCause());
        }
    }

    /**
     * @param document
     * @param fieldValue
     * @return String com o conte�do indexado
     */
    private String addFieldFilePathToDocument(Document document, Object fieldValue) {
        document.add(new Field(FoxIndexerSearcherIF.SEARCH_FIELD_FILE_PATH, (String) fieldValue, Field.Store.YES, Field.Index.UN_TOKENIZED));
        return (String) fieldValue;
    }

    /**
     * @param document
     * @param fieldMapping
     * @param fieldValue
     * @return String com o conte�do indexado
     * @throws FoxLuceneConverterException
     */
    private String addFieldFileToDocument(Document document, FoxFieldMappingIF fieldMapping, Object fieldValue) throws FoxLuceneConverterException {
        String value = StringUtil.STRING_EMPTY;
        try {
            File file = new File(fieldValue.toString());
            FoxIndexTypeIF type = FoxIndexTypeFactory.getFoxIndexType(fieldMapping.getIndexType());
            FoxDocumentIF foxDoc = FoxDocumentFactory.getDocument(file, type);
            value = foxDoc.getText();
            if (value != null) {
                document.add(new Field(fieldMapping.getName(), value, Field.Store.NO, Field.Index.TOKENIZED));
            }
            return value;
        } catch (Exception e) {
            throw new FoxLuceneConverterException(e);
        }
    }

    /**
     * @param object
     * @param document
     * @param fieldMapping
     * @param kreator
     * @param className
     * @param nullable
     * @return
     * @throws FoxObjectCreaterException
     * @throws FoxLuceneConverterException
     */
    private String addFieldKeyWordToDocument(Document document, FoxFieldMappingIF foxFieldMapping, Object fieldValue, String className) throws FoxObjectCreaterException, FoxLuceneConverterException {
        String value = fieldValue.toString();
        if (foxFieldMapping.isKeyField().booleanValue()) {
            value = className + fieldValue;
            document.add(new Field(FoxIndexerSearcherIF.SEARCH_FIELD_KEY_NAME, foxFieldMapping.getName(), Field.Store.YES, Field.Index.UN_TOKENIZED));
        }
        document.add(new Field(foxFieldMapping.getName(), value, Field.Store.YES, Field.Index.UN_TOKENIZED));
        return value;
    }

    /**
     * @param document
     * @param fieldMapping
     * @param fieldValue
     * @return String com o conte�do indexado
     */
    private String addFieldNumericToDocument(Document document, FoxFieldMappingIF fieldMapping, Object fieldValue) {
        String value = fieldValue.toString();
        document.add(new Field(fieldMapping.getName(), value, Field.Store.YES, Field.Index.UN_TOKENIZED));
        return value;
    }

    private Document addFieldsDefaul(Document document, FoxClassMappingIF classMapping, Object contents, String className) throws FoxLuceneConverterException {
        if (className != null) {
            document.add(new Field(FoxIndexerSearcherIF.SEARCH_FIELD_CLASS_NAME, className, Field.Store.YES, Field.Index.UN_TOKENIZED));
        } else {
            throw new FoxLuceneConverterException(FoxLuceneConverterException.CLASS_NAME_IS_NULL_EXCEPTION);
        }
        if (classMapping.getFoxLanguage() != null) {
            document.add(new Field(FoxIndexerSearcherIF.SEARCH_FIELD_LANGUAGE, classMapping.getFoxLanguage(), Field.Store.YES, Field.Index.UN_TOKENIZED));
        } else {
            throw new FoxLuceneConverterException(FoxLuceneConverterException.LANGUAGE_NAME_IS_NULL_EXCEPTION);
        }
        if ((contents != null) && (contents != StringUtil.STRING_SPACING)) {
            document.add(new Field(FoxIndexerSearcherIF.SEARCH_FIELD_CONTENTS, (String) contents, Field.Store.NO, Field.Index.TOKENIZED));
            return document;
        } else {
            return null;
        }
    }

    /**
     * @param document
     * @param fieldMapping
     * @param fieldValue
     * @return String com o conte�do indexado
     */
    private String addFieldFullTextToDocument(Document document, FoxFieldMappingIF fieldMapping, Object fieldValue) {
        String value = fieldValue.toString();
        document.add(new Field(fieldMapping.getName(), value, Field.Store.YES, Field.Index.TOKENIZED));
        return value;
    }

    /**
     * @param document
     * @param fieldMapping
     * @param fieldValue
     * @return String com o conte�do indexado
     */
    private String addFieldTextToDocument(Document document, FoxFieldMappingIF fieldMapping, Object fieldValue) {
        String value = fieldValue.toString();
        document.add(new Field(fieldMapping.getName(), value, Field.Store.NO, Field.Index.TOKENIZED));
        return value;
    }

    /**
     * @param document
     * @param fieldMapping
     * @param contents
     * @param fieldValue
     * @param className
     * @param indexTypeByte
     * @return
     * @throws FoxObjectCreaterException
     * @throws FoxLuceneConverterException
     * @throws FoxMappingException
     */
    private String addFieldToDocument(Document document, FoxFieldMappingIF fieldMapping, String contents, Object fieldValue, String className) throws FoxObjectCreaterException, FoxLuceneConverterException {
        if (fieldMapping.isKeyField().booleanValue()) {
            addFieldKeyWordToDocument(document, fieldMapping, fieldValue, className);
        } else {
            switch(FoxIndexTypeFactory.getFoxIndexType(fieldMapping.getIndexType()).getCode()) {
                case FoxIndexTypeIF.BYTE_INDEX_TYPE_KEYWORD:
                    {
                        return addFieldKeyWordToDocument(document, fieldMapping, fieldValue, className);
                    }
                case FoxIndexTypeIF.BYTE_INDEX_TYPE_DATE:
                    {
                        return addFieldDateToDocument(document, fieldMapping, fieldValue);
                    }
                case FoxIndexTypeIF.BYTE_INDEX_TYPE_NUMERIC:
                    {
                        return addFieldNumericToDocument(document, fieldMapping, fieldValue);
                    }
                case FoxIndexTypeIF.BYTE_INDEX_TYPE_FULL_TEXT:
                    {
                        return addFieldFullTextToDocument(document, fieldMapping, fieldValue);
                    }
                case FoxIndexTypeIF.BYTE_INDEX_TYPE_TEXT:
                    {
                        return addFieldTextToDocument(document, fieldMapping, fieldValue);
                    }
                case FoxIndexTypeIF.BYTE_FILE_TYPE_DOC:
                case FoxIndexTypeIF.BYTE_FILE_TYPE_HTML:
                case FoxIndexTypeIF.BYTE_FILE_TYPE_PDF:
                case FoxIndexTypeIF.BYTE_FILE_TYPE_RTF:
                case FoxIndexTypeIF.BYTE_FILE_TYPE_TXT:
                    {
                        String filePath = ((File) fieldValue).getAbsolutePath();
                        contents += StringUtil.STRING_SPACING + addFieldFilePathToDocument(document, filePath);
                        contents += StringUtil.STRING_SPACING + addFieldFileToDocument(document, fieldMapping, fieldValue);
                        return contents;
                    }
            }
        }
        return contents;
    }

    /**
     * Converte objetos HITS do Apache Lucune uma cole��o de objeto FoxResult
     * 
     * @param hits -
     *                Hits de retorno da busca do Lucene.
     * @param foxMapping -
     *                Objeto com as lista de classes mapeadas.
     * @return Collection de FoxResult
     * @throws FoxLuceneConverterException
     */
    public Collection<FoxResultIF> converterHitsToFoxResult(Hits hits, FoxMappingIF foxMapping, String textSearch, String fieldSearchName) throws FoxLuceneConverterException {
        FoxResultIF result;
        Collection<FoxResultIF> results = new ArrayList<FoxResultIF>();
        String fieldName;
        String className;
        try {
            log.info("Inicia a varredura dos HITs. Total = " + hits.length());
            for (int i = 0; i < hits.length(); i++) {
                result = FoxResultFactory.getFoxResult(foxMapping.getFoxConfiguration());
                Document doc = hits.doc(i);
                result.setBoost(doc.getBoost());
                className = doc.get(FoxIndexerSearcherIF.SEARCH_FIELD_CLASS_NAME);
                log.info("Pega o nome da classe do objeto : " + className);
                if ((className != null) && !className.equals(StringUtil.STRING_EMPTY)) {
                    log.info("Pega a classe do objeto.");
                    FoxClassMappingIF classFoxMapping = foxMapping.getClassMappingByName(className);
                    if (classFoxMapping != null) {
                        log.info("Adiciona o objeto convertido no objeto resultado.");
                        result.setObject(this.convertLuceneDocumentToObject(doc, classFoxMapping));
                        log.info("Lista todos os campos mapeados e retorna na busca.");
                        for (Iterator<?> fildInter = doc.getFields().iterator(); fildInter.hasNext(); ) {
                            fieldName = ((Field) fildInter.next()).name();
                            log.info("Pega o campo atual: " + fieldName);
                            log.debug("Cria o FieldResult e adiciona ao resultado.");
                            FoxResultFieldIF foxResultField = FoxResultFactory.getFoxResultField(foxMapping.getFoxConfiguration());
                            foxResultField.setName(fieldName);
                            foxResultField.setValue(doc.get(fieldName));
                            result.addResultField(foxResultField);
                        }
                        log.debug("Remove o campo cont�udo - este campo � interno ao fox");
                        result.deleteResultField(new FoxLuceneResultField(FoxIndexerSearcherIF.SEARCH_FIELD_CONTENTS, doc.get(FoxIndexerSearcherIF.SEARCH_FIELD_CONTENTS)));
                        result.setQuery(fieldSearchName + StringUtil.STRING_EQUALS + textSearch);
                    } else {
                        throw new FoxLuceneConverterException(FoxMappingException.CLASS_MAPPER_NOT_FOUND_EXCEPTION);
                    }
                }
                results.add(result);
            }
            return results;
        } catch (IOException e) {
            throw new FoxLuceneConverterException(FoxLuceneConverterException.HITS_TO_FIELD_EXECPTION, e);
        } catch (FoxResultFieldException e) {
            throw new FoxLuceneConverterException(e);
        } catch (FoxObjectCreaterException e) {
            throw new FoxLuceneConverterException("Erro na cria��o do Result ou do ResultField!", e);
        }
    }

    /**
     * Converte um objeto qualquer, a partir de um objeto ClassMapping em um
     * Document do Apache Lucene.
     * 
     * @param object -
     *                Objeto a ser convertido.
     * @param classMapping -
     *                Objeto com o mepeamento para a convers�o o object.
     * @return Um Document do Apache Lucene.
     * @throws FoxLuceneConverterException
     * @throws FoxDocumentException
     */
    public Document converterObjectToLuceneDocument(Object object, FoxClassMappingIF classMapping) throws FoxLuceneConverterException, FoxDocumentException {
        Document document = new Document();
        FoxFieldMappingIF fieldMapping;
        String contents = StringUtil.STRING_EMPTY;
        String fieldContents = StringUtil.STRING_EMPTY;
        Object fieldValue = null;
        String className = verifyIsNullObject(object);
        FoxObjectCreator kreator = new FoxObjectCreator(className);
        for (Iterator<FoxFieldMappingIF> fieldsMapping = classMapping.getFieldsMapping().iterator(); fieldsMapping.hasNext(); ) {
            fieldMapping = fieldsMapping.next();
            try {
                fieldValue = kreator.getFieldValue(fieldMapping.getName(), object);
                if (verifyNullable(fieldMapping, fieldValue)) {
                    fieldContents += StringUtil.STRING_SPACING + addFieldToDocument(document, fieldMapping, contents, fieldValue, className);
                    contents += (fieldMapping.isSearchable()) ? fieldContents : StringUtil.STRING_EMPTY;
                }
            } catch (FoxObjectCreaterException e) {
                throw new FoxLuceneConverterException(e);
            }
        }
        return addFieldsDefaul(document, classMapping, contents, className);
    }

    /**
     * Converte um objeto Document do Apache Lucene para um Objeto.
     * 
     * @param document
     *                Documento do Apache Lucene.
     * @param classMapping
     *                Objeto com o mapeamento da classe do objeto que ser�
     *                gerado.
     * @return Retorna um objeto do tipo informado no objeto classMapping.
     * @throws FoxLuceneConverterException
     *                 TODO IMPLEMENTACAO - Adicionar o logging nesta classe.
     */
    private Object convertLuceneDocumentToObject(Document document, FoxClassMappingIF classMapping) throws FoxLuceneConverterException {
        String className = document.get(FoxIndexerSearcherIF.SEARCH_FIELD_CLASS_NAME);
        log.info("Pega a classe do objeto = " + className);
        Object object;
        FoxObjectCreator creator = null;
        String docFieldValue;
        try {
            if (className != null && !className.equals(StringUtil.STRING_EMPTY)) {
                log.info("Verifica se o nome da classe � " + "igual ao nome da classe mepeada = " + className + StringUtil.STRING_EQUALS + classMapping.getClassPath());
                if (className.equals(classMapping.getClassPath())) {
                    creator = new FoxObjectCreator(className);
                    object = creator.createObject();
                    log.info("Cria o objeto a partir do nome da classe = " + object);
                    String fieldType;
                    log.info("Varre todos os campos do mapeamento" + " buscando o valor no document lucene");
                    for (Iterator<FoxFieldMappingIF> fieldMapping = classMapping.getFieldsMapping().iterator(); fieldMapping.hasNext(); ) {
                        FoxFieldMappingIF field = fieldMapping.next();
                        docFieldValue = document.get(field.getName());
                        if (field.getName().equals(document.get(FoxIndexerSearcherIF.SEARCH_FIELD_KEY_NAME))) {
                            docFieldValue = docFieldValue.substring(className.length(), docFieldValue.length());
                        }
                        if ((docFieldValue != null) && !docFieldValue.toString().equals(StringUtil.STRING_EMPTY)) {
                            try {
                                fieldType = field.getType();
                                if (fieldType.equals("java.io.File")) {
                                } else {
                                    if (fieldType.endsWith(".Date")) {
                                        FoxLuceneDateConverter luceneDateConverter = new FoxLuceneDateConverter(FoxLuceneDateConverter.DEFAULT_LUCENE_DATA_FORMAT);
                                        creator.setFieldValue(field.getName(), fieldType, luceneDateConverter.convertDateLuceneToDate(docFieldValue), object);
                                    } else {
                                        creator.setFieldValue(field.getName(), fieldType, (Object) docFieldValue, object);
                                    }
                                }
                            } catch (Exception e) {
                                throw new FoxLuceneConverterException("SetField " + field.getName() + StringUtil.STRING_EQUALS + docFieldValue + StringUtil.STRING_EQUALS + e.getMessage(), e.getCause());
                            }
                        }
                    }
                } else {
                    log.warn(FoxObjectCreaterException.CLASS_NOT_FOUND_EXECTPION);
                    return null;
                }
            } else {
                throw new FoxLuceneConverterException(FoxObjectCreaterException.CLASS_NAME_IS_NULL_EXECTION);
            }
        } catch (Exception e) {
            throw new FoxLuceneConverterException(e.getMessage(), e.getCause());
        }
        log.info("Retornou o objeto = " + object);
        return object;
    }

    /**
     * Verifica se o objeto � nulo.
     * 
     * @param object
     *                Objeto a ser checado
     * @return String Nome da classe
     * @throws FoxLuceneConverterException
     */
    private String verifyIsNullObject(Object object) throws FoxLuceneConverterException {
        if (null == object) {
            throw new FoxLuceneConverterException(FoxLuceneConverterException.OBJECT_IS_NULL_EXCEPTION);
        }
        return object.getClass().getName();
    }

    /**
     * Verifica se o valor de um campo pode ser nulo.
     * 
     * @param fieldMapping
     * @param fieldValue
     * @return Retorna true se o objeto n�o est� nulo.
     * @throws FoxLuceneConverterException
     *                 Se o objeto for obrigat�rio e estiver com valor nulo.
     */
    public boolean verifyNullable(FoxFieldMappingIF fieldMapping, Object fieldValue) throws FoxLuceneConverterException {
        boolean isNull = fieldValue == null;
        if (isNull && !fieldMapping.isNullable()) {
            throw new FoxLuceneConverterException(FoxLuceneConverterException.FIELD_NULL_EXCEPTION + StringUtil.STRING_EQUALS + fieldMapping.getName());
        }
        return !isNull;
    }
}
