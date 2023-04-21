package br.com.edawir.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *  Essa classe fornece um acesso em n�vel de aplica��o para o
 *  Spring ApplicationContext. O ApplicationContext �
 *  Injetado em um m�todo est�tico da classe "AppContext"
 *  
 *  Use AppContext.getApplicationContext() para ter acesso
 *  a todos os Beans.
 *  
 *  {@link http://blog.jdevelop.eu/2008/07/06/access-the-spring-applicationcontext-from-everywhere-in-your-application/}
 *  @author Grupo EDAWIR
 *  @since 09/11/2011
 */
public class ApplicationContextProvider implements ApplicationContextAware {

    /**
     * M�todo que atualiza o contexto da aplica��o
     * 
     * @param applicationContext contexto da aplica��o
     * @throws Exce��o de Bean
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        AppContext.setApplicationContext(applicationContext);
    }
}
