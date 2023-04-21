package org.itx.equipment.l2;

import java.io.IOException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.itx.equipment.l1.Bills;
import org.itx.equipment.l1.Complects;
import org.itx.equipment.l1.Contractors;
import org.itx.equipment.l1.Destinations;
import org.itx.equipment.l1.Distributions;
import org.itx.equipment.l1.Divisions;
import org.itx.equipment.l1.Jobs;
import org.itx.equipment.l1.Juridicals;
import org.itx.equipment.l1.Opers;
import org.itx.equipment.l1.Parts;
import org.itx.equipment.l1.PaymentOrders;
import org.itx.equipment.l1.Procurements;
import org.itx.equipment.l1.Regulations;
import org.itx.equipment.l1.Types;
import org.jabsorb.JSONRPCBridge;

public class EQJSONServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private Divisions divisions;

    private Complects complects;

    private Parts parts;

    private Types types;

    private Juridicals juridicals;

    private Bills bills;

    private Procurements procurements;

    private PaymentOrders paymentOrders;

    private Opers opers;

    private Destinations destinations;

    private Contractors contractors;

    private Distributions distributions;

    private Regulations regulations;

    private Jobs jobs;

    public void init(ServletConfig config) throws ServletException {
        try {
            super.init(config);
            InitialContext context = new InitialContext();
            this.juridicals = ((Juridicals) context.lookup(Juridicals.class.getSimpleName() + "/remote"));
            this.bills = ((Bills) context.lookup(Bills.class.getSimpleName() + "/remote"));
            this.procurements = ((Procurements) context.lookup(Procurements.class.getSimpleName() + "/remote"));
            this.paymentOrders = ((PaymentOrders) context.lookup(PaymentOrders.class.getSimpleName() + "/remote"));
            this.divisions = ((Divisions) context.lookup(Divisions.class.getSimpleName() + "/remote"));
            this.complects = ((Complects) context.lookup(Complects.class.getSimpleName() + "/remote"));
            this.parts = ((Parts) context.lookup(Parts.class.getSimpleName() + "/remote"));
            this.types = ((Types) context.lookup(Types.class.getSimpleName() + "/remote"));
            this.opers = ((Opers) context.lookup(Opers.class.getSimpleName() + "/remote"));
            this.destinations = ((Destinations) context.lookup(Destinations.class.getSimpleName() + "/remote"));
            this.contractors = ((Contractors) context.lookup(Contractors.class.getSimpleName() + "/remote"));
            this.distributions = ((Distributions) context.lookup(Distributions.class.getSimpleName() + "/remote"));
            this.regulations = ((Regulations) context.lookup(Regulations.class.getSimpleName() + "/remote"));
            this.jobs = ((Jobs) context.lookup(Jobs.class.getSimpleName() + "/remote"));
            System.out.println("init");
        } catch (NamingException e) {
            System.err.println("EQJSONServlet.init context lookup problem");
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        show(req, resp);
    }

    private void show(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = null;
        HttpSession session = req.getSession(true);
        if (session == null) {
            url = "/error.jsp";
        } else {
            JSONRPCBridge json_bridge = null;
            json_bridge = (JSONRPCBridge) session.getAttribute("JSONRPCBridge");
            if (json_bridge == null) {
                json_bridge = new JSONRPCBridge();
                session.setAttribute("JSONRPCBridge", json_bridge);
            }
            json_bridge.registerObject("EqJuridicals", this.juridicals);
            json_bridge.registerObject("EqBills", this.bills);
            json_bridge.registerObject("EqProcurements", this.procurements);
            json_bridge.registerObject("EqPaymentOrders", this.paymentOrders);
            json_bridge.registerObject("EqDivisions", this.divisions);
            json_bridge.registerObject("EqTypes", this.types);
            json_bridge.registerObject("EqParts", this.parts);
            json_bridge.registerObject("EqComplects", this.complects);
            json_bridge.registerObject("EqOpers", this.opers);
            json_bridge.registerObject("EqDestinations", this.destinations);
            json_bridge.registerObject("EqContractors", this.contractors);
            json_bridge.registerObject("EqDistributions", this.distributions);
            json_bridge.registerObject("EqRegulations", this.regulations);
            json_bridge.registerObject("EqJobs", this.jobs);
        }
    }
}
