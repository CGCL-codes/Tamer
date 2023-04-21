package br.com.rapidrest.request;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.com.rapidrest.chain.BasicChainContext;
import br.com.rapidrest.chain.BeanCommand;
import br.com.rapidrest.chain.Chain;
import br.com.rapidrest.chain.Command;
import br.com.rapidrest.chain.ELBindingCommand;
import br.com.rapidrest.chain.IoCCommand;
import br.com.rapidrest.chain.JspResponseCommand;
import br.com.rapidrest.chain.MethodFinderCommand;
import br.com.rapidrest.chain.MethodInvokeCommand;
import br.com.rapidrest.chain.PageRedirectCommand;
import br.com.rapidrest.chain.RequestScopeCommand;
import br.com.rapidrest.chain.ResponseGeneratorCommand;
import br.com.rapidrest.chain.RestURLRedirectCommand;
import br.com.rapidrest.chain.SimpleResponseGeneratorCommand;
import br.com.rapidrest.chain.URLRedirectCommand;
import br.com.rapidrest.ioc.FieldHandler;
import br.com.rapidrest.ioc.MessagesFieldHandler;
import br.com.rapidrest.ioc.RequestFieldHandler;

public class BasicRequesHandler implements RequestHandler {

    private static final long serialVersionUID = 6402781062017259035L;

    public static final String WEB_PACKAGE = "rapidrest.webpackage";

    @Override
    public Chain createChain(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        BasicChainContext context = new BasicChainContext();
        context.setRequest(request);
        context.setResponse(response);
        context.setFilterChain(filterChain);
        context.setWebPackage(getWebPackage(request));
        ArrayList<Command> commands = new ArrayList<Command>();
        commands.add(new RequestScopeCommand());
        commands.add(new BeanCommand());
        commands.add(new MethodFinderCommand());
        IoCCommand ioCCommand = new IoCCommand();
        for (FieldHandler handler : getFieldHandlers()) {
            ioCCommand.addHandler(handler);
        }
        commands.add(ioCCommand);
        commands.add(new ELBindingCommand());
        commands.add(new MethodInvokeCommand());
        commands.add(new PageRedirectCommand());
        commands.add(new RestURLRedirectCommand());
        commands.add(new URLRedirectCommand());
        commands.add(new SimpleResponseGeneratorCommand());
        commands.add(new ResponseGeneratorCommand());
        commands.add(new JspResponseCommand());
        Chain chain = new Chain(commands);
        chain.setContext(context);
        return chain;
    }

    protected List<FieldHandler> getFieldHandlers() {
        List<FieldHandler> handlers = new ArrayList<FieldHandler>();
        handlers.add(new RequestFieldHandler());
        handlers.add(new MessagesFieldHandler());
        return handlers;
    }

    protected String getWebPackage(HttpServletRequest request) {
        String webPackage = request.getSession().getServletContext().getInitParameter(WEB_PACKAGE);
        return webPackage;
    }
}
