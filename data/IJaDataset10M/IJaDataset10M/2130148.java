package br.ita.autowidget.defaultgeneration;

import br.ita.autowidget.metadata.ComponentMetadata;

/**
 * Mecanismo padr�o de gera��o de interfaces com o usu�rio. 
 * <p>Disponibiliza dois m�todos para esse fim, um para visualiza��o e
 * outro para edi��o de um determinado objeto.</p>
 * <p>Analisa o ComponentName do objeto ComponentMetadata informado e, dessa forma, 
 * faz a sele��o do mecanismo mais adequado para tal componente.</p>
 * <p>Abaixo desse objeto se encontra um strategy <code>IComponentTemplate</code> que far� a gera��o da interface
 * propriamente dita</p>
 * @author Nando
 *
 * @param <E>
 */
public interface IDefaultComponentTemplate<E> {

    /**
	 * Selects the default ComponentTemplate for "display" rendering
	 * @param beanMetadata
	 * @return content to be rendered
	 */
    E display(ComponentMetadata beanMetadata);

    /**
	 * Selects the default ComponentTemplate for "edit" rendering
	 * @param beanMetadata
	 * @return content to be rendered
	 */
    E edit(ComponentMetadata beanMetadata);

    /**
	 * Realiza a sele��o do mecanismo de gera��o de interfaces com o usu�rio mais adequado para o 
	 * componenten corrente.
	 * @param beanMetadata
	 * @return
	 */
    IComponentTemplate<E> selectTemplate(ComponentMetadata beanMetadata);
}
