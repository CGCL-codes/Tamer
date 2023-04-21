package dsrwebserver.pages.dsr.admin;

import java.sql.ResultSet;
import ssmith.html.HTMLFunctions;
import ssmith.lang.Dates;
import dsrwebserver.components.MainLayout;
import dsrwebserver.pages.AbstractHTMLPage;
import dsrwebserver.tables.ClientErrorsTable;

public class showerrors extends AbstractHTMLPage {

    public showerrors() {
        super();
    }

    @Override
    public void process() throws Exception {
        StringBuffer str = new StringBuffer();
        if (this.session.isLoggedIn()) {
            if (this.current_login.isAdmin()) {
                int del = this.headers.getGetValueAsInt("del");
                if (del > 0) {
                    ClientErrorsTable.DeleteRec(dbs, del);
                }
                str.append("<table class=\"stats\" width=\"100%\">");
                HTMLFunctions.StartRow(str);
                HTMLFunctions.AddCellHeading(str, "Date");
                HTMLFunctions.AddCellHeading(str, "Version");
                HTMLFunctions.AddCellHeading(str, "Error");
                HTMLFunctions.AddCellHeading(str, "Login");
                HTMLFunctions.AddCellHeading(str, "Delete?");
                HTMLFunctions.EndRow(str);
                String sql = "SELECT * FROM ClientErrors ORDER BY DateCreated";
                ResultSet rs = dbs.getResultSet(sql);
                while (rs.next()) {
                    HTMLFunctions.StartRow(str);
                    HTMLFunctions.AddCell(str, Dates.FormatDate(rs.getTimestamp("DateCreated"), Dates.UKDATE_FORMAT_WITH_TIME));
                    HTMLFunctions.AddCell(str, "" + rs.getFloat("Version"));
                    HTMLFunctions.AddCell(str, rs.getString("ErrorText"));
                    HTMLFunctions.AddCell(str, rs.getString("Login"));
                    HTMLFunctions.AddCell(str, "<a href=\"?del=" + rs.getInt("ClientErrorID") + "\">Delete</a>");
                    HTMLFunctions.EndRow(str);
                }
                HTMLFunctions.EndTable(str);
                this.body_html.append(MainLayout.GetHTML(this, "Client Errors", str));
            } else {
                this.redirectTo_Using303("/");
            }
        } else {
            this.redirectTo_Using303("/");
        }
    }
}
