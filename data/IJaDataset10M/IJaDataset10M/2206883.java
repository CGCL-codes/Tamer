package hcmus.am;

import hcmus.am.client.entity.ThietBiEntity;
import hcmus.am.client.entity.ThongSoLoaiThietBiEntity;
import hcmus.am.client.entity.ThongSoThietBiEntity;
import hcmus.am.dao.LoaiThietBiDao;
import hcmus.am.dao.ThietBiDao;
import hcmus.am.dao.ThongSoLoaiThietBiDAO;
import hcmus.am.dao.ThongSoThietBiDao;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AC2
 */
public class ComputerInfoCollection extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /***
	 * ThongSo[i][0]: name of thiet bi
	 * THongSo[i][1->n]: ThongSo of thiet bi 
	 */
    public static String[][] ThongSo = new String[][] { { "MAINBOARD", "MODEL" }, { "CPU", "SPEED" }, { "RAM", "CAPACITY", "TYPE", "SPEED", "SLOTID" }, { "GPU", "CHIPSET", "MEMORY" }, { "MONITOR", "TYPE", "SERIALNUMBER" }, { "SOUNDCARD" }, { "KEYBOARD" }, { "HARDDISK", "MODEL", "SIZE" }, { "CD,DVD", "MODEL" }, { "NETWORKADAPTER", "SPEED", "MAC", "IP" }, { "PRINTER", "DRIVER" }, { "OS", "VERSION", "COMMENT" }, { "MOUSE" } };

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ComputerInfoCollection() {
        super();
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ram = (String) request.getParameter("RAM");
        response.getWriter().println(ram);
    }
}
