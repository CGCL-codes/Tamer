package BD;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.*;

public class BDPack {

    private String server;

    private String user;

    private String pass;

    java.sql.Connection db_connection;

    public Statement stmt;

    public enum TipoBusqueda {

        DNI, NOMBRE, APELLIDOS
    }

    ;

    /**
	 * Constructor para almacenar los datos para una conexión mediante JDBC a la una BD
	 * 
	 * @param server Url del servidor
	 * @param user Nombre de usuario
	 * @param pass Contraseña del usuario
	 */
    public BDPack(String server, String user, String pass) {
        this.server = server;
        this.user = user;
        this.pass = pass;
        db_connection = null;
        stmt = null;
    }

    /**
	 * Esta función nos permite entrar en el sistema como usuario registrado.
	 * 
	 * @return Nos devuelve false o true, dependiendo de si se ha podido acceder al sistema o no.
	 */
    public Boolean conectar() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            return false;
        }
        try {
            db_connection = DriverManager.getConnection(server, user, pass);
        } catch (SQLException e) {
            return false;
        }
        try {
            stmt = db_connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
	 * Esta función nos permite abandonar la BD de forma segura.
	 * 
	 * @return Nos devuelve false o true, dependiendo de si se ha podido salir o no.
	 */
    public Boolean desconectar() {
        try {
            stmt.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
            return false;
        }
        try {
            db_connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
	 * Funcion para añadir un empleado a la base de datos.
	 * 
	 * @param empleado Empleado con la informacion para almacenar en la base de datos.
	 * @return Devuelve true para indicar que a tenido exito al almacenar la informacion en la base de datos.
	 * @throws SQLException 
	 */
    public Boolean add_empleado(Empleado empleado) throws SQLException {
        boolean exito;
        String dni = empleado.getDni();
        String nombre = empleado.getNombre();
        String apellidos = empleado.getApellidos();
        String password = empleado.getPassword();
        String direccion = empleado.getDireccion();
        String localidad = empleado.getLocalidad();
        String provincia = empleado.getProvincia();
        String cp = empleado.getCp();
        String email = empleado.getEmail();
        String telefono = empleado.getTelefono();
        String numeroSS = empleado.getNumeroSS();
        Boolean activo = empleado.getActivo();
        Statement stmt = db_connection.createStatement();
        try {
            stmt.executeUpdate("INSERT INTO Persona (DNI, Nombre, Apellidos, Contraseña, Direccion, Localidad, Provincia, CP, Telefono, Email)" + "VALUES ('" + dni + "', '" + nombre + "', '" + apellidos + "', '" + password + "', '" + direccion + "', '" + localidad + "', '" + provincia + "', '" + cp + "', '" + telefono + "', '" + email + "')");
            stmt.executeUpdate("INSERT INTO Empleado (DNI, Activo, NumeroSS)" + "VALUES ('" + dni + "', " + activo + ", '" + numeroSS + "')");
            stmt.executeUpdate("DELETE FROM Rol WHERE DNI=" + dni);
            Rol r = null;
            for (int i = 0; i < empleado.getRol().size(); i++) {
                r = empleado.getRol().get(i);
                stmt.executeUpdate("INSERT INTO Rol (DNI, FechaAlta, Tipo)" + "VALUES ('" + dni + "', '" + r.getFechaAlta().toString() + "', '" + r.getTipo() + "')");
            }
            stmt.executeUpdate("DELETE FROM Turno WHERE DNI=" + dni);
            for (int i = 0; i < empleado.getHorario().size(); i++) {
                add_horario(dni, empleado.getHorario().get(i));
            }
            exito = true;
        } catch (SQLException e) {
            e.printStackTrace();
            exito = false;
        }
        return exito;
    }

    /**
	 * Funcion para modificar los datos de un empleado.
	 * 
	 * @param dni DNI del empleado que se desea modificar los datos.
	 * @param empleado Empleado con la informacion modificada del empleado.
	 * @return Devuelve true para indicar que la modificacion se a realizado con exito.
	 * @throws SQLException 
	 */
    public Boolean modificar_empleado(String dni, Empleado empleado) throws SQLException {
        boolean exito;
        String nombre = empleado.getNombre();
        String apellidos = empleado.getApellidos();
        String password = empleado.getPassword();
        String direccion = empleado.getDireccion();
        String localidad = empleado.getLocalidad();
        String provincia = empleado.getProvincia();
        String cp = empleado.getCp();
        String email = empleado.getEmail();
        String telefono = empleado.getTelefono();
        String numeroSS = empleado.getNumeroSS();
        Boolean activo = empleado.getActivo();
        Statement stmt = db_connection.createStatement();
        try {
            stmt.executeUpdate("UPDATE Persona SET " + "DNI = '" + dni + "', " + "Nombre = '" + nombre + "', " + "Apellidos = '" + apellidos + "', " + "Contraseña = '" + password + "', " + "Direccion = '" + direccion + "', " + "Localidad = '" + localidad + "', " + "Provincia = '" + provincia + "', " + "CP = '" + cp + "', " + "Telefono = '" + telefono + "', " + "Email = '" + email + "' " + "WHERE DNI = '" + dni + "'");
            stmt.executeUpdate("UPDATE Empleado SET " + "DNI = '" + dni + "', " + "Activo = " + activo + ", " + "NumeroSS = '" + numeroSS + "' " + "WHERE DNI = '" + dni + "'");
            stmt.executeUpdate("DELETE FROM Rol WHERE DNI=" + dni);
            Rol r = null;
            for (int i = 0; i < empleado.getRol().size(); i++) {
                r = empleado.getRol().get(i);
                stmt.executeUpdate("INSERT INTO Rol (DNI, FechaAlta, Tipo)" + "VALUES ('" + dni + "', '" + r.getFechaAlta().toString() + "', '" + r.getTipo() + "')");
            }
            stmt.executeUpdate("DELETE FROM Turno WHERE DNI=" + dni);
            for (int i = 0; i < empleado.getHorario().size(); i++) {
                add_horario(dni, empleado.getHorario().get(i));
            }
            exito = true;
        } catch (SQLException e) {
            e.printStackTrace();
            exito = false;
        }
        return exito;
    }

    /**
	 * Funcion que devuelve una lista con los empleados que cumplen la condicion de la busqueda.
	 * 
	 * @param tipo Indica el tipo de busqueda.
	 * @param valor Indica el valor que debe tener el dato del empleado buscado.
	 * @return Devuelve una lista con los empleados que cumplen la condicion de la busqueda.
	 * @throws SQLException 
	 */
    public List<Empleado> consultar_empleado(TipoBusqueda tipo, String valor) throws SQLException {
        List<Empleado> l = new ArrayList<Empleado>();
        Empleado em = new Empleado();
        List<Rol> lr = new ArrayList<Rol>();
        List<Turno> lt = new ArrayList<Turno>();
        Statement stmt = db_connection.createStatement();
        String consulta = "";
        if (tipo == TipoBusqueda.DNI) {
            consulta = "SELECT * " + "FROM Persona p, Empleado e " + "WHERE p.DNI = e.DNI AND p.DNI LIKE '%" + valor + "%'";
        } else if (tipo == TipoBusqueda.NOMBRE) {
            consulta = "SELECT * " + "FROM Persona p, Empleado e " + "WHERE p.DNI = e.DNI AND p.Nombre LIKE '%" + valor + "%'";
        } else if (tipo == TipoBusqueda.APELLIDOS) {
            consulta = "SELECT * " + "FROM Persona p, Empleado e " + "WHERE p.DNI = e.DNI AND p.Apellidos LIKE '%" + valor + "%'";
        }
        try {
            ResultSet rs = stmt.executeQuery(consulta);
            while (rs.next()) {
                em = new Empleado();
                em.setDni(rs.getString(1));
                em.setNombre(rs.getString(2));
                em.setApellidos(rs.getString(3));
                em.setPassword(rs.getString(4));
                em.setDireccion(rs.getString(5));
                em.setLocalidad(rs.getString(6));
                em.setProvincia(rs.getString(7));
                em.setCp(rs.getString(8));
                em.setTelefono(rs.getString(9));
                em.setEmail(rs.getString(10));
                em.setActivo(rs.getBoolean(12));
                em.setNumeroSS(rs.getString(13));
                lr = consultar_rol(em.getDni());
                em.setListRol(lr);
                lt = consultar_turno(em.getDni());
                em.setListTurno(lt);
                l.add(em);
            }
        } catch (SQLException e) {
            l = null;
        }
        return l;
    }

    /**
	 * Funcion para añadir un paciente a la base de datos.
	 * 
	 * @param paciente Paciente con la informacion para almacenar en la base de datos.
	 * @return Devuelve true para indicar que a tenido exito al almacenar la informacion en la base de datos.
	 * @throws SQLException 
	 */
    public Boolean add_paciente(Paciente paciente) throws SQLException {
        boolean exito;
        String dni = paciente.getDni();
        String nombre = paciente.getNombre();
        String apellidos = paciente.getApellidos();
        String password = paciente.getPassword();
        String direccion = paciente.getDireccion();
        String localidad = paciente.getLocalidad();
        String provincia = paciente.getProvincia();
        String cp = paciente.getCp();
        String email = paciente.getEmail();
        String telefono = paciente.getTelefono();
        Boolean activo = paciente.getActivo();
        java.sql.Date fechaAlta = paciente.getFechaAlta();
        Statement stmt = db_connection.createStatement();
        try {
            stmt.executeUpdate("INSERT INTO Persona (DNI, Nombre, Apellidos, Contraseña, Direccion, Localidad, Provincia, CP, Telefono, Email)" + "VALUES ('" + dni + "', '" + nombre + "', '" + apellidos + "', '" + password + "', '" + direccion + "', '" + localidad + "', '" + provincia + "', '" + cp + "', '" + telefono + "', '" + email + "')");
            stmt.executeUpdate("INSERT INTO Paciente (DNI, Activo, FechaAlta)" + "VALUES ('" + dni + "', " + activo + ", '" + fechaAlta + "')");
            exito = true;
        } catch (SQLException e) {
            exito = false;
            e.printStackTrace();
        }
        return exito;
    }

    /**
	 * Funcion para modificar los datos de un paciente.
	 * 
	 * @param dni DNI del paciente que se desea modificar los datos.
	 * @param paciente Paciente con la informacion modificada del paciente.
	 * @return Devuelve true para indicar que la modificacion se a realizado con exito.
	 * @throws SQLException 
	 */
    public Boolean modificar_paciente(String dni, Paciente paciente) throws SQLException {
        boolean exito;
        String nombre = paciente.getNombre();
        String apellidos = paciente.getApellidos();
        String password = paciente.getPassword();
        String direccion = paciente.getDireccion();
        String localidad = paciente.getLocalidad();
        String provincia = paciente.getProvincia();
        String cp = paciente.getCp();
        String telefono = paciente.getTelefono();
        String email = paciente.getEmail();
        Boolean activo = paciente.getActivo();
        java.sql.Date fechaAlta = paciente.getFechaAlta();
        Statement stmt = db_connection.createStatement();
        try {
            stmt.executeUpdate("UPDATE Persona SET " + "DNI = '" + dni + "', " + "Nombre = '" + nombre + "', " + "Apellidos = '" + apellidos + "', " + "Contraseña = '" + password + "', " + "Direccion = '" + direccion + "', " + "Localidad = '" + localidad + "', " + "Provincia = '" + provincia + "', " + "CP = '" + cp + "', " + "Telefono = '" + telefono + "', " + "Email = '" + email + "' " + "WHERE DNI = '" + dni + "'");
            stmt.executeUpdate("UPDATE Paciente SET " + "DNI = '" + dni + "', " + "Activo = " + activo + ", " + "FechaAlta = '" + fechaAlta + "' " + "WHERE DNI = '" + dni + "'");
            exito = true;
        } catch (SQLException e) {
            exito = false;
        }
        return exito;
    }

    /**
	 * Funcion que devuelve una lista con los pacientes que cumplen la condicion de la busqueda.
	 * 
	 * @param tipo Indica el tipo de busqueda.
	 * @param valor Indica el valor que debe tener el dato del empleado buscado.
	 * @return Devuelve una lista con los pacientes que cumplen la condicion de la busqueda.
	 * @throws SQLException 
	 */
    public List<Paciente> consultar_paciente(TipoBusqueda tipo, String valor) throws SQLException {
        List<Paciente> l = new ArrayList<Paciente>();
        Paciente p = new Paciente();
        Statement stmt = db_connection.createStatement();
        String consulta = "";
        if (tipo == TipoBusqueda.DNI) {
            consulta = "SELECT * " + "FROM Persona per, Paciente pac " + "WHERE per.DNI = pac.DNI AND per.DNI LIKE '%" + valor + "%'";
        } else if (tipo == TipoBusqueda.NOMBRE) {
            consulta = "SELECT * " + "FROM Persona per, Paciente pac " + "WHERE per.DNI = pac.DNI AND per.Nombre LIKE '%" + valor + "%'";
        } else if (tipo == TipoBusqueda.APELLIDOS) {
            consulta = "SELECT * " + "FROM Persona per, Paciente pac " + "WHERE per.DNI = pac.DNI AND per.Apellidos LIKE '%" + valor + "%'";
        }
        try {
            ResultSet rs = stmt.executeQuery(consulta);
            while (rs.next()) {
                p = new Paciente();
                p.setDni(rs.getString(1));
                p.setNombre(rs.getString(2));
                p.setApellidos(rs.getString(3));
                p.setPassword(rs.getString(4));
                p.setDireccion(rs.getString(5));
                p.setLocalidad(rs.getString(6));
                p.setProvincia(rs.getString(7));
                p.setCp(rs.getString(8));
                p.setTelefono(rs.getString(9));
                p.setEmail(rs.getString(10));
                p.setActivo(rs.getBoolean(12));
                p.setFechaAlta(rs.getDate(13));
                l.add(p);
            }
        } catch (SQLException e) {
            l = null;
        }
        return l;
    }

    /**
	 * Funcion para añadir un turno a un empleado.
	 * 
	 * @param dni DNI del empleado al que se le va a añadir un turno.
	 * @param turno Informacion del turno que se le asigna al empleado.
	 * @return Devuelve true para indicar que a tenido exito al almacenar la informacion en la base de datos.
	 * @throws SQLException 
	 */
    public Boolean add_horario(String dni, Turno turno) throws SQLException {
        Boolean exito;
        String horaEntrada = turno.getHoraEntrada();
        String horaSalida = turno.getHoraSalida();
        String dia = turno.getDia();
        Statement stmt = db_connection.createStatement();
        try {
            stmt.executeUpdate("INSERT INTO Turno (DNI, Dia, HoraEntrada, HoraSalida)" + "VALUES ('" + dni + "', '" + dia + "', '" + horaEntrada + "', '" + horaSalida + "')");
            exito = true;
        } catch (SQLException e) {
            e.printStackTrace();
            exito = false;
        }
        return exito;
    }

    /**
	 * Funcion para eliminiar un turno a un empleado.
	 * 
	 * @param dni DNI del empleado al que se le va a quitar un turno.
	 * @param turno Informacion del turno que se desea eliminar.
	 * @return Devuelve true para indicar que se a podido eliminar el turno indicado.
	 * @throws SQLException 
	 */
    public Boolean eliminar_horario(String dni, Turno turno) throws SQLException {
        Boolean exito;
        String horaEntrada = turno.getHoraEntrada();
        String dia = turno.getDia();
        Statement stmt = db_connection.createStatement();
        try {
            stmt.executeUpdate("DELETE FROM Turno " + "WHERE DNI = '" + dni + "' AND Dia = '" + dia + "' AND HoraEntrada = '" + horaEntrada + "'");
            exito = true;
        } catch (SQLException e) {
            exito = false;
        }
        return exito;
    }

    /**
	 * Funcion que devuelve una lista con los roles de un empleado deteminado por su dni.
	 * 
	 * @param dni DNI del empleado que se desea conocer sus roles.
	 * @return Devuelve una lista con los roles del empleado indicado.
	 * @throws SQLException 
	 */
    private List<Rol> consultar_rol(String dni) throws SQLException {
        List<Rol> l = new ArrayList<Rol>();
        Rol r = new Rol();
        Statement stmt = db_connection.createStatement();
        String consulta = "SELECT * " + "FROM Rol " + "WHERE DNI = '" + dni + "'";
        try {
            ResultSet rs = stmt.executeQuery(consulta);
            while (rs.next()) {
                r = new Rol();
                r.setFechaAlta(rs.getDate(2));
                r.setFechaBaja(rs.getDate(3));
                r.setTipo(rs.getString(4));
                l.add(r);
            }
        } catch (SQLException e) {
            l = null;
        }
        return l;
    }

    /**
	 * Funcion que devuelve una lista con los turnos de un empleado deteminado por su dni.
	 * 
	 * @param dni DNI del empleado que se desea conocer sus turnos.
	 * @return Devuelve una lista con los turnos del empleado indicado.
	 * @throws SQLException 
	 */
    private List<Turno> consultar_turno(String dni) throws SQLException {
        List<Turno> l = new ArrayList<Turno>();
        Turno t = new Turno();
        Statement stmt = db_connection.createStatement();
        String consulta = "SELECT * " + "FROM Turno " + "WHERE DNI = '" + dni + "'";
        try {
            ResultSet rs = stmt.executeQuery(consulta);
            while (rs.next()) {
                t = new Turno();
                t.setDia(rs.getString(2));
                t.setHoraEntrada(rs.getString(3));
                t.setHoraSalida(rs.getString(4));
                l.add(t);
            }
        } catch (SQLException e) {
            l = null;
        }
        return l;
    }
}
