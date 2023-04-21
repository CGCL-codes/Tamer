package com.iona.servlets;

import java.io.IOException;
import java.util.List;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.iona.dao.AbilitiesDAO;
import com.iona.domain.Abilities;
import com.iona.json.JSONException;
import com.iona.json.builders.AbilitiesBuilder;

/**
 * Servlet implementation class AbilitiesFeederServlet
 */
@WebServlet("/AbilitiesFeederServlet")
public class AbilitiesFeederServlet extends FeederServlet implements Servlet {

    private static final long serialVersionUID = 1L;

    @Override
    public Object doFeed(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
        List<Abilities> findAll = new AbilitiesDAO().findAll();
        return new AbilitiesBuilder(findAll).build();
    }
}
