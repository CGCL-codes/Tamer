package com.simconomy.magic.servlet;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.simconomy.magic.controller.DetailController;
import com.simconomy.magic.exceptions.CardPriceException;
import com.simconomy.magic.model.CardDetail;
import com.simconomy.magic.service.CardPriceService;

public class DetailServlet extends HttpServlet {

    private static Logger log = Logger.getLogger(DetailController.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        log.info("enter DetailServlet name = '" + name + "'");
        CardPriceService cardPriceService = CardPriceService.Util.getInstance();
        List<CardDetail> cards;
        try {
            cards = cardPriceService.retrieveCards(name);
            req.setAttribute("cardDetails", cards);
            getServletConfig().getServletContext().getRequestDispatcher("/WEB-INF/jsp/detail.jsp").forward(req, resp);
        } catch (CardPriceException e) {
            throw new RuntimeException(e);
        }
    }
}
