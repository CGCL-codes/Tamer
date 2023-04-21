package org.jcompany.model.adm;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.jcompany.commons.PlcArgEntity;
import org.jcompany.commons.PlcBaseEntity;
import org.jcompany.commons.PlcConstantsCommons;
import org.jcompany.commons.PlcException;
import org.jcompany.commons.PlcHqlInteractveEntity;
import org.jcompany.model.PlcBaseManager;
import org.jcompany.persistence.PlcBaseDAO;
import org.jcompany.persistence.hibernate.PlcBaseHibernateDAO;

/**
 * jCompany 1.5: Classe que gerencia o utilit�rio de Hibernate Query Language (HQL) Interativo
 *
 * Permitir ao desenvolvedor fazer queries interativas no padr�o Hibernate. Esta op��o �
 * disponibilizada, por default, como um item de menu visivel somente para usuarios com
 * a role "Area Tecnica", e colocado juntamente com a aplica��o.<p>
 *
 * Importante: Aplica��es que exijam seguran�a de informa��es devem remover este item no
 * deployment para produ��o
 *
 * @since jCompany 1.5
 * @version $Id: PlcHqlInteractiveManager.java,v 1.3 2006/05/17 20:47:40 rogerio_baldini Exp $
 * @author Cl�udia Seara
 */
public class PlcHqlInteractiveManager extends PlcBaseManager {

    int recursive = 0;

    PlcBaseDAO baseDAO;

    /**
	 * jCompany 3.0 Construtor com Injecao de Dependencia
	 */
    public PlcHqlInteractiveManager(PlcBaseHibernateDAO baseDAO) {
        this.baseDAO = baseDAO;
    }

    /**
	 * jCompany 1.5: Sobrep�e o m�todo padr�o do ancestral, que realiza queries autom�ticas
	 * em padr�o QBE, para realizar as queries din�micas necess�rias ao HQL Interativo.
	 *
	 * Exibe as propriedades da classe principal e um n�vel al�m, incluindo propriedades das
	 * classes agregadas one-to-many e many-to-one (detalhes e classes de lookup)
	 * @deprecated Utilizar o plugin Hibernate Console para este recurso
	 * @throws PlcException Exce��es s�o convertidas para o padr�o
	 *                   do jCompany, para tratamento gen�rico e exibi��o para usu�rio.
	 *                   Uma exce��o deve ser disparada caso nenhuma ocorr�ncia seja encontrada.
	 */
    @Override
    public List retrieveListQBE(Class classMain, String orderByDynamic, List argsQBE) throws PlcException {
        if (log.isDebugEnabled()) log.debug(" retrieveList - e = " + classMain.getName());
        recursive = 0;
        List listHqlInteractive = new ArrayList();
        ArrayList selectionResult = new ArrayList();
        try {
            String commandHQL = null;
            PlcArgEntity entityArg = null;
            for (int i = 0; i < argsQBE.size(); i++) {
                entityArg = (PlcArgEntity) argsQBE.get(i);
                if (entityArg.getType().equals(PlcConstantsCommons.CONSULTATION.QBE.QBE_TYPE_ARGUMENT)) {
                    log.info(" retrieveList - entityArg name = " + entityArg.getName());
                    log.info(" retrieveList - entityArg value = " + entityArg.getValue());
                    log.info(" retrieveList - entityArg operator = " + entityArg.getOperator());
                    log.info(" retrieveList - entityArg type = " + entityArg.getType());
                    if (entityArg.getName().equals("commandSelection")) {
                        commandHQL = entityArg.getValue();
                    }
                }
            }
            if (commandHQL != null) {
                log.info(" retrieveList - before iterate - commandHQL = " + commandHQL);
                Object resultLine = null;
                Object[] resultObject = null;
                String nameClassSuperior = null;
                ArrayList columnValues = new ArrayList();
                int counterRecord = 1;
                String delimiterRecord = "-------------------------------------------------- Record: ";
                String delimiterLine = "\n";
                List l = ((PlcBaseHibernateDAO) baseDAO).executeHQL(commandHQL);
                Iterator result = l.iterator();
                while (result.hasNext()) {
                    selectionResult.add(delimiterLine + delimiterRecord + counterRecord);
                    resultLine = result.next();
                    if (resultLine.getClass().isArray()) {
                        resultObject = (Object[]) resultLine;
                    } else {
                        resultObject = new Object[1];
                        resultObject[0] = resultLine;
                    }
                    for (int i = 0; i < resultObject.length; i++) {
                        columnValues = (ArrayList) organizeColumns(resultObject[i], nameClassSuperior, 1);
                        Iterator it = columnValues.iterator();
                        while (it.hasNext()) selectionResult.add(it.next());
                    }
                    counterRecord++;
                }
            }
            PlcHqlInteractveEntity hqlInt = null;
            Iterator it = selectionResult.iterator();
            while (it.hasNext()) {
                hqlInt = new PlcHqlInteractveEntity();
                hqlInt.setSelectionCommand(commandHQL);
                hqlInt.setSelectionResult((String) it.next());
                listHqlInteractive.add(hqlInt);
            }
            return listHqlInteractive;
        } catch (Exception ex) {
            throw new PlcException("jcompany.errors.hqlinterativo", new Object[] { ex }, ex, log);
        }
    }

    /**
	 * jCompany 1.5: Monta a exibi��o de um objeto agregado ao mestre,
	 * em conformidade com o type de cada propriedade.
	 *
	 * @param objectResult Objeto recuperado (uma inst�ncia)
	 * @param nameClassSuperior Tipo do Value Object a ser recuperado. Deve conter o package completo
	 *                 da classe. Ex: com.empresa.app.vo.TipoCurso.
	 * @param level evita recursividade para mais de um n�vel (indicador de n�vel)
	 *
	 * @return List Lista de linhas a serem exibidas
	 *
	 * @throws Exception Somente repassa exce��es
	 */
    private List organizeColumns(Object objectResult, String nameClassSuperior, int level) throws Exception {
        ArrayList columnValues = new ArrayList();
        ArrayList columnValuesRecursive = new ArrayList();
        recursive++;
        if (recursive > 10) {
            log.error("Recursive iteration was obstructed by security roles.");
            return columnValues;
        }
        try {
            String delimiterLine = "\n";
            Class clazz = objectResult.getClass();
            String className = null;
            if (nameClassSuperior == null) className = clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1); else className = nameClassSuperior + "." + clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1);
            log.info(" organizeColumns - class name = " + className + "  -  level = " + level);
            if (PlcBaseEntity.class.isAssignableFrom(clazz)) {
                BeanInfo info = Introspector.getBeanInfo(clazz);
                PropertyDescriptor[] pd = info.getPropertyDescriptors();
                if (pd != null) {
                    Method method = null;
                    Class[] parameters = null;
                    Object retObj = null;
                    for (int i = 0; i < pd.length; i++) {
                        method = pd[i].getReadMethod();
                        if (method != null) {
                            parameters = method.getParameterTypes();
                            retObj = method.invoke(objectResult, (Object[]) parameters);
                        }
                        if (retObj == null) {
                            columnValues.add(delimiterLine + className + "." + pd[i].getName() + " = null ");
                        } else {
                            if (PlcBaseEntity.class.isAssignableFrom(pd[i].getPropertyType().getClass())) {
                                if (level <= 1) {
                                    columnValuesRecursive = (ArrayList) organizeColumns(retObj, className, level + 1);
                                    Iterator itrec = columnValuesRecursive.iterator();
                                    while (itrec.hasNext()) columnValues.add(itrec.next());
                                }
                            } else {
                                if (pd[i].getPropertyType().getName().equals("java.util.Set")) {
                                    Set objectInstances = (Set) retObj;
                                    Iterator it = objectInstances.iterator();
                                    while (it.hasNext()) {
                                        columnValuesRecursive = (ArrayList) organizeColumns(it.next(), className, level - 1);
                                        Iterator itrec = columnValuesRecursive.iterator();
                                        while (itrec.hasNext()) columnValues.add(itrec.next());
                                    }
                                } else {
                                    columnValues.add(delimiterLine + className + "." + pd[i].getName() + " = " + retObj.toString());
                                }
                            }
                        }
                    }
                }
            } else {
                columnValues.add(delimiterLine + className + " = " + objectResult.toString());
            }
            return columnValues;
        } catch (Exception ex) {
            throw new PlcException("jcompany.errors.hqlinterativo", new Object[] { ex }, ex, log);
        }
    }
}
