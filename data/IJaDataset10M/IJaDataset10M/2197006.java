package router.swingx.cad;

public class Cad extends javax.swing.JPanel {

    public static final long serialVersionUID = 191L;

    public Cad() {
        super();
        this.to_reconstruct();
    }

    private void to_reconstruct() {
        jToolBar_superior = new javax.swing.JToolBar();
        Button_router = new javax.swing.JButton();
        Button_switch = new javax.swing.JButton();
        Button_cable = new javax.swing.JButton();
        Button_internet = new javax.swing.JButton();
        Button_pc = new javax.swing.JButton();
        Button_server = new javax.swing.JButton();
        jToolBar_superior.setFloatable(false);
        jToolBar_superior.setRollover(false);
        Button_router.setBorder(null);
        Button_router.setToolTipText(router.swingx.util.Constantes_Swingx.ROUTE_NAME_DEFAULT);
        this.Button_router.setIcon(new javax.swing.ImageIcon(getClass().getResource(router.swingx.util.Constantes_Swingx.RESOURCE_CAD_ROUTER)));
        this.jToolBar_superior.add(Button_router);
        Button_switch.setBorder(null);
        Button_switch.setToolTipText(router.swingx.util.Constantes_Swingx.SWITCH_NAME_DEFAULT);
        this.Button_switch.setIcon(new javax.swing.ImageIcon(getClass().getResource(router.swingx.util.Constantes_Swingx.RESOURCE_CAD_SWITCH)));
        this.jToolBar_superior.add(Button_switch);
        Button_cable.setBorder(null);
        Button_cable.setToolTipText(router.swingx.util.Constantes_Swingx.CABLE_NAME_DEFAULT);
        this.Button_cable.setIcon(new javax.swing.ImageIcon(getClass().getResource(router.swingx.util.Constantes_Swingx.RESOURCE_CAD_CABLE)));
        this.jToolBar_superior.add(Button_cable);
        Button_internet.setBorder(null);
        Button_internet.setToolTipText(router.swingx.util.Constantes_Swingx.INTERNET_NAME_DEFAULT);
        this.Button_internet.setIcon(new javax.swing.ImageIcon(getClass().getResource(router.swingx.util.Constantes_Swingx.RESOURCE_CAD_INTERNET)));
        this.jToolBar_superior.add(Button_internet);
        Button_pc.setBorder(null);
        Button_pc.setToolTipText(router.swingx.util.Constantes_Swingx.PC_NAME_DEFAULT);
        this.Button_pc.setIcon(new javax.swing.ImageIcon(getClass().getResource(router.swingx.util.Constantes_Swingx.RESOURCE_CAD_PC)));
        this.jToolBar_superior.add(Button_pc);
        Button_server.setBorder(null);
        Button_server.setToolTipText(router.swingx.util.Constantes_Swingx.SERVIDOR_NAME_DEFAULT);
        this.Button_server.setIcon(new javax.swing.ImageIcon(getClass().getResource(router.swingx.util.Constantes_Swingx.RESOURCE_CAD_SERVER)));
        this.jToolBar_superior.add(Button_server);
        jToolBar_superior.setBounds(0, 0, 1152, 30);
        jToolBar_superior.setBackground(new java.awt.Color(245, 245, 245));
        this.setBackground(new java.awt.Color(255, 255, 255));
        this.add(jToolBar_superior);
        this.setLayout(null);
    }

    public javax.swing.JButton getButton_router() {
        return this.Button_router;
    }

    public javax.swing.JButton getButton_switch() {
        return this.Button_switch;
    }

    public javax.swing.JButton getButton_cable() {
        return this.Button_cable;
    }

    public javax.swing.JButton getButton_internet() {
        return this.Button_internet;
    }

    public javax.swing.JButton getButton_pc() {
        return this.Button_pc;
    }

    public javax.swing.JButton getButton_server() {
        return this.Button_server;
    }

    /**
	 * Aqui se ingresa al router
	 * @param id
	 * @param x
	 * @param y
	 */
    public void agregarJRouter(int id, int num, int x, int y) {
        router1 = new router.swingx.object.FigRouter(id, num);
        this.add(router1);
        dispositivos.add(router1);
        java.awt.event.MouseListener cable_botton = new java.awt.event.MouseListener() {

            public void mouseClicked(java.awt.event.MouseEvent e) {
                System.out.println("fsfdsgdsgffsffdsfsfsgsgfsgf");
            }

            public void mouseEntered(java.awt.event.MouseEvent e) {
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
            }

            public void mousePressed(java.awt.event.MouseEvent e) {
                router1.setBounds(e.getX(), e.getY(), 130, 150);
                repaint();
            }

            public void mouseReleased(java.awt.event.MouseEvent e) {
                router1.setBounds(e.getX(), e.getY(), 130, 150);
                repaint();
            }
        };
        router1.addMouseListener(cable_botton);
        router1.setBounds(x, y, 130, 150);
        router1.setVisible(true);
    }

    /**
	 * Aqui se guarda en array Los dispositivos
	 * @return java.util.Collection
	 */
    public java.util.Collection getArrayDispositivos() {
        return this.dispositivos;
    }

    /**
	 * Aqui se ingresa a la pc
	 * @param id
	 * @param x
	 * @param y
	 */
    public void agregarJPc(int id, int num, int x, int y) {
        pc1 = new router.swingx.object.FigPc(id, num);
        this.add(pc1);
        dispositivos.add(pc1);
        pc1.setBounds(x, y, 80, 80);
        pc1.setVisible(true);
        pc1 = null;
    }

    /**
	 * Aqui se ingresa el internet
	 * @param nombre
	 * @param x
	 * @param y
	 */
    public void agregarJInternet(String nombre, int x, int y) {
        internet1 = new router.swingx.object.FigInternet(nombre);
        this.add(internet1);
        dispositivos.add(internet1);
        internet1.setBounds(x, y, 120, 120);
        internet1.setVisible(true);
        internet1 = null;
    }

    /**
	 * Aqui se ingresa el servidor
	 * @param id
	 * @param x
	 * @param y
	 */
    public void agregarJServer(int id, int num, int x, int y) {
        server1 = new router.swingx.object.FigServer(id, num);
        this.add(server1);
        dispositivos.add(server1);
        server1.setBounds(x, y, 120, 160);
        server1.setVisible(true);
        server1 = null;
    }

    /**
	 * Aqui se ingresa el switch
	 * @param id
	 * @param x
	 * @param y
	 */
    public void agregarJSwitch(int id, int x, int y) {
        switch1 = new router.swingx.object.FigSwitch(id);
        this.add(switch1);
        dispositivos.add(switch1);
        switch1.setBounds(x, y, 120, 120);
        switch1.setVisible(true);
        switch1 = null;
    }

    public void agregarJCable(int id, int x, int y) {
        cable1 = new router.swingx.object.FigCable(id);
        this.add(cable1);
        puntosX = x;
        puntosY = y;
        cable1.setBounds(x, y, 0, 0);
        cable1.setVisible(true);
    }

    public void terminoJCable(int x, int y, boolean fin) {
        int temp1 = 0;
        int temp2 = 0;
        int temp3 = 0;
        int temp4 = 0;
        if (y > puntosY) {
            temp1 = y;
            temp2 = puntosY;
        }
        if (x > puntosX) {
            temp3 = x;
            temp4 = puntosX;
        }
        if (y < puntosY) {
            temp2 = y;
            temp1 = puntosY;
        }
        if (x < puntosX) {
            temp4 = x;
            temp3 = puntosX;
        }
        cable1.setSize(temp3 - temp4, temp1 - temp2);
        if (fin) {
            cable1 = null;
            puntosX = 0;
            puntosY = 0;
        }
        repaint();
    }

    /**
	 * se borra toda absolutamente todo
	 *
	 */
    public void erase_todo() {
        dispositivos = new java.util.ArrayList();
        this.removeAll();
        this.add(jToolBar_superior);
        jToolBar_superior.setVisible(true);
        this.repaint();
    }

    /**
	 * Aca se podram no ver el JToolBar
	 */
    public void setInVisible_JToolBar() {
        jToolBar_superior.setVisible(false);
        this.Button_internet.setEnabled(true);
        this.repaint();
    }

    private int puntosX = 0;

    private int puntosY = 0;

    private java.util.ArrayList dispositivos = new java.util.ArrayList();

    private router.swingx.object.FigRouter router1;

    private router.swingx.object.FigCable cable1;

    private router.swingx.object.FigPc pc1;

    private router.swingx.object.FigInternet internet1;

    private router.swingx.object.FigServer server1;

    private router.swingx.object.FigSwitch switch1;

    private javax.swing.JToolBar jToolBar_superior;

    private javax.swing.JButton Button_router;

    private javax.swing.JButton Button_switch;

    private javax.swing.JButton Button_cable;

    private javax.swing.JButton Button_internet;

    private javax.swing.JButton Button_pc;

    private javax.swing.JButton Button_server;
}
