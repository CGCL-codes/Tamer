package com.dna.motion.filters;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.directwebremoting.AjaxFilter;
import org.directwebremoting.AjaxFilterChain;
import com.dna.motion.dao.VariableDAO;
import com.dna.motion.entities.Page;
import com.dna.motion.entities.Translation;
import com.dna.motion.entities.Variable;
import com.dna.motion.tools.Response;

/**
 * The Class VariablesFilter.
 */
public class VariablesFilter implements AjaxFilter {

    public Object doFilter(Object obj, Method method, Object[] params, AjaxFilterChain chain) throws Exception {
        Object objectReturned = chain.doFilter(obj, method, params);
        return objectReturned;
    }

    /**
	 * Replace variables by values.
	 * 
	 * @param page
	 *            the page
	 */
    private void replaceVariablesByValues(Page page) {
        VariableDAO dao = new VariableDAO();
        String html_content = page.getHtmlContent();
        String css_content = page.getCssContent();
        String js_content = page.getJsContent();
        List<Variable> variables = dao.getAll().getResponse();
        Map<String, Variable> mapVariables = this.mapVariables(variables);
        this.process(html_content, mapVariables);
        this.process(css_content, mapVariables);
        this.process(js_content, mapVariables);
    }

    /**
	 * Map variables.
	 * 
	 * @param variables
	 *            the variables
	 * @return the map
	 */
    private Map<String, Variable> mapVariables(List<Variable> variables) {
        Map<String, Variable> mapOfVariables = new HashMap<String, Variable>();
        for (Variable v : variables) {
            mapOfVariables.put(v.getName(), v);
        }
        return mapOfVariables;
    }

    private void process(String content, Map<String, Variable> mapVariables) {
        StringTokenizer tokens = new StringTokenizer(content, " ", true);
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken();
            if (token.toUpperCase().contains("VALUE={")) {
                String newToken = new String(token);
                newToken = newToken.toLowerCase();
                newToken.replace("value={", "");
                newToken.replace("}", "");
                Variable var = mapVariables.get(newToken);
                content.replace(token, var.getValue());
            }
        }
    }
}
