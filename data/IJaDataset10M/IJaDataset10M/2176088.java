package pub.servlets;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import pub.db.AlleleTable;
import pub.utils.*;
import pub.db.*;

public class AddAllele extends DatabasePubServlet {

    public void doService(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = null;
        action = request.getParameter("add.allele");
        FormErrors errors = new FormErrors();
        if (action == null) {
            redirectToEmptyForm(request, response);
        } else {
            validate(errors, request);
            if (errors.size() > 0) {
                request.setAttribute("pub.servlets.errors", errors);
                redirectToErrorPage(request, response);
            } else {
                runAddCommand(request);
                redirectToResultsPage(request, response);
            }
        }
    }

    public void validate(FormErrors errors, HttpServletRequest request) {
        String[] params = new String[] { "name" };
        for (int i = 0; i < params.length; i++) {
            if (request.getParameter(params[i]) == null || request.getParameter(params[i]).trim().equals("")) {
                String message = "required";
                errors.add(params[i], message);
            }
        }
        String checkExisting = isExistingInDb(request.getParameter("name"));
        if (checkExisting.trim().length() > 0) {
            String url = StringUtils.getBaseContextPath(request) + "/DisplayAllele?allele_id=" + checkExisting;
            errors.add("Allele <font color=red>" + request.getParameter("name"), "</font>existed in DB table <A href='" + url + "'>" + checkExisting + "</A>");
        }
    }

    /**
     * Return pub_allele_id if is existing
     */
    public String isExistingInDb(String allele_name) {
        AlleleTable allele_table = new AlleleTable(conn);
        return allele_table.lookup("name", allele_name);
    }

    private void redirectToEmptyForm(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        addChoicesToRequest(request);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/jsp/addnew/AddAllele.jsp");
        dispatcher.include(request, response);
    }

    private void redirectToErrorPage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/jsp/form_error.jsp");
        dispatcher.include(request, response);
    }

    private void runAddCommand(HttpServletRequest request) throws IOException, ServletException {
        AlleleTable table = new pub.db.AlleleTable(conn);
        HashMap column_values = new HashMap();
        String user_id = Login.getUserId(request);
        String[] params = new String[] { "name", "type", "inheritance", "polymorphic_sequence", "insertion_type", "construct_type", "allele_mode", "mutation_site", "mutagen" };
        for (int i = 0; i < params.length; i++) {
            if (request.getParameter(params[i]) != null && request.getParameter(params[i]).trim().length() > 0) {
                column_values.put(params[i], (request.getParameter(params[i])).trim());
            }
        }
        String new_id = null;
        try {
            new_id = table.addEntry(column_values);
            if (new_id != null && new_id.length() > 0) {
                table.updateThisField(new_id, "entered_by", user_id, user_id);
                table.updateThisField(new_id, "updated_by", user_id, user_id);
            }
        } catch (java.sql.SQLException e) {
            System.err.println("Error in adding entry to allele table from servelt/AddAllele");
        }
        if (new_id != null && new_id.length() > 0) {
            request.setAttribute("inserted_id", new_id);
            if (request.getParameter("fromAccession") != null && request.getParameter("fromAccession").trim().length() > 0 && !request.getParameter("fromAccession").equalsIgnoreCase("null")) {
                String fromAccession = request.getParameter("fromAccession");
                addToLinkingTable(new_id, fromAccession, user_id, request);
            }
        }
    }

    private void addToLinkingTable(String allele_id, String fromAccession, String user_id, HttpServletRequest request) {
        GeneralDB table;
        HashMap col_values;
        String new_id;
        String table_name = StringUtils.parseAccessionGetTableName(fromAccession);
        String table_id = StringUtils.parseAccessionGetTableId(fromAccession);
        table = null;
        if (table_name.length() < 0 || table_id.length() < 0) return;
        col_values = new HashMap();
        if (table_name.equals("germplasm")) {
            table = new AlleleGermplasmLinkingTable(conn);
            col_values.put("pub_germplasm_id", table_id);
            col_values.put("pub_allele_id", allele_id);
        } else if (table_name.equals("gene")) {
            table = new GeneAlleleLinkingTable(conn);
            col_values.put("pub_gene_id", table_id);
            col_values.put("pub_allele_id", allele_id);
        }
        HttpSession session = request.getSession(false);
        if (session != null) {
            if (session.getValue("addAllele_genotype") != null) {
                col_values.put("genotype", (String) session.getValue("addAllele_genotype"));
                session.removeValue("addAllele_genotype");
            }
            if (session.getValue("addAllele_segregation") != null) {
                col_values.put("segregation_ratio", (String) session.getValue("addAllele_segregation"));
                session.removeValue("addAllele_segregation");
            }
        }
        new_id = null;
        try {
            new_id = table.addEntry(col_values);
            if (new_id != null && new_id.length() > 0) {
                table.updateThisField(new_id, "entered_by", user_id, user_id);
                table.updateThisField(new_id, "updated_by", user_id, user_id);
            }
        } catch (SQLException e) {
            System.err.println("Error in adding entry to allele linking table from servelt/AddAllele");
        }
    }

    private void redirectToResultsPage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        addChoicesToRequest(request);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/DisplayAllele?allele_id=" + request.getAttribute("inserted_id"));
        dispatcher.include(request, response);
    }

    private Map addEmptyChoice(Map m) {
        m.put("--Empty--", "");
        return m;
    }

    private void addChoicesToRequest(HttpServletRequest request) {
        AlleleTable alleledb = new AlleleTable(conn);
        request.setAttribute("alleleChoices", addEmptyChoice(ListUtils.listAsHash(alleledb.allTypes())));
        request.setAttribute("inheritanceChoices", addEmptyChoice(ListUtils.listAsHash(alleledb.allInheritances())));
        request.setAttribute("insertionChoices", addEmptyChoice(ListUtils.listAsHash(alleledb.allInsertionTypes())));
        request.setAttribute("constructChoices", addEmptyChoice(ListUtils.listAsHash(alleledb.allConstructTypes())));
        request.setAttribute("alleleModeChoices", addEmptyChoice(ListUtils.listAsHash(alleledb.allAlleleModes())));
        request.setAttribute("alleleMutationSites", addEmptyChoice(ListUtils.listAsHash(alleledb.allMutationSites())));
        request.setAttribute("alleleMutagens", addEmptyChoice(ListUtils.listAsHash(alleledb.allMutagens())));
    }
}
