package br.com.linkcom.neo.core.web.init;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import br.com.linkcom.neo.exception.CouldNotCreateDataSourceException;

/**
 * @author rogelgarcia
 * @since 17/07/2006
 * @version 1.0
 */
public interface DataSourceConfigStrategy {

    /**
	 * Configura o dataSource da aplica��o
	 * @param beanFactory
	 * @return true indicando se o dataSource foi criado ou se o dataSource j� existia e nao foi modificado
	 * @throws CouldNotCreateDataSourceException Se ocorrer algum erro na cria��o do DataSource
	 */
    boolean configureDataSource(ConfigurableListableBeanFactory beanFactory) throws CouldNotCreateDataSourceException;
}
