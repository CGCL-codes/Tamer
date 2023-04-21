package backup.respaldodatosinicial;

import datos.archivo.AlumnoMateriaDictadoPersistenteEnDisco;
import datos.archivo.test.*;
import datos.archivo.AlumnoPersistenteEnDisco;
import datos.archivo.ExamenPersistenteEnDisco;
import datos.archivo.MateriaDictadoPersistenteEnDisco;
import datos.archivo.MateriaPersistenteEnDisco;
import datos.archivo.ProfesorPersistenteEnDisco;
import datos.coneccion.mysql.AlumnoConsultasMySQL;
import datos.coneccion.mysql.BaseDatosMySQL;
import datos.coneccion.mysql.ExamenConsultasMySQL;
import datos.coneccion.mysql.MateriaConsultasMySQL;
import datos.coneccion.mysql.MateriaDictadoConsultasMySQL;
import datos.coneccion.mysql.ProfesorConsultasMySQL;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import objetos.constantes.Configuracion;
import objetos.entidades.Alumno;
import objetos.entidades.Examen;
import objetos.entidades.Materia;
import objetos.entidades.MateriaDictado;
import objetos.entidades.Profesor;

/**
 *
 * @author luciano
 */
public class DatosArchivoGuardarRespaldoTest {

    public static void guardarAlumnos() {
        Configuracion conf = new Configuracion();
        BaseDatosMySQL baseDatosMySQL = new BaseDatosMySQL();
        baseDatosMySQL.setDireccionServer(conf.getDireccionServer());
        baseDatosMySQL.setNombreBaseDatos(conf.getNombreBaseDatos());
        baseDatosMySQL.setPuerto(conf.getPuerto());
        baseDatosMySQL.setUsuario(conf.getUsuario());
        baseDatosMySQL.setPass(conf.getPass());
        AlumnoConsultasMySQL alumnoConsultasMySQL = new AlumnoConsultasMySQL(baseDatosMySQL);
        AlumnoPersistenteEnDisco alumnoPersistenteEnDisco = new AlumnoPersistenteEnDisco();
        Date fecha = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HHmmss");
        try {
            List<Alumno> listaAlumno = alumnoConsultasMySQL.getAll();
            alumnoPersistenteEnDisco.setListaAlumnos(listaAlumno);
            alumnoPersistenteEnDisco.setNombreArchivo("alumnosBackup" + ".txt");
            alumnoPersistenteEnDisco.guardar();
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        } catch (SQLException ex) {
            System.out.println(ex);
        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
        }
    }

    public static void guardarProfesores() {
        Configuracion conf = new Configuracion();
        BaseDatosMySQL baseDatosMySQL = new BaseDatosMySQL();
        baseDatosMySQL.setDireccionServer(conf.getDireccionServer());
        baseDatosMySQL.setNombreBaseDatos(conf.getNombreBaseDatos());
        baseDatosMySQL.setPuerto(conf.getPuerto());
        baseDatosMySQL.setUsuario(conf.getUsuario());
        baseDatosMySQL.setPass(conf.getPass());
        ProfesorConsultasMySQL profesorConsultasMySQL = new ProfesorConsultasMySQL(baseDatosMySQL);
        ProfesorPersistenteEnDisco profesorPersistenteEnDisco = new ProfesorPersistenteEnDisco();
        Date fecha = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HHmmss");
        try {
            List<Profesor> listaProfesores = profesorConsultasMySQL.getAll();
            profesorPersistenteEnDisco.setListaProfesores(listaProfesores);
            profesorPersistenteEnDisco.setNombreArchivo("profesoresBackup" + ".txt");
            profesorPersistenteEnDisco.guardar();
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        } catch (SQLException ex) {
            System.out.println(ex);
        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
        }
    }

    public static void guardarMaterias() {
        Configuracion conf = new Configuracion();
        BaseDatosMySQL baseDatosMySQL = new BaseDatosMySQL();
        baseDatosMySQL.setDireccionServer(conf.getDireccionServer());
        baseDatosMySQL.setNombreBaseDatos(conf.getNombreBaseDatos());
        baseDatosMySQL.setPuerto(conf.getPuerto());
        baseDatosMySQL.setUsuario(conf.getUsuario());
        baseDatosMySQL.setPass(conf.getPass());
        MateriaConsultasMySQL materiaConsultasMySQL = new MateriaConsultasMySQL(baseDatosMySQL);
        MateriaPersistenteEnDisco materiaPersistenteEnDisco = new MateriaPersistenteEnDisco();
        Date fecha = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HHmmss");
        try {
            List<Materia> listaProfesores = materiaConsultasMySQL.getAll();
            materiaPersistenteEnDisco.setListaMaterias(listaProfesores);
            materiaPersistenteEnDisco.setNombreArchivo("materiasBackup" + ".txt");
            materiaPersistenteEnDisco.guardar();
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        } catch (SQLException ex) {
            System.out.println(ex);
        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
        }
    }

    public static void guardarMateriasDictados() {
        Configuracion conf = new Configuracion();
        BaseDatosMySQL baseDatosMySQL = new BaseDatosMySQL();
        baseDatosMySQL.setDireccionServer(conf.getDireccionServer());
        baseDatosMySQL.setNombreBaseDatos(conf.getNombreBaseDatos());
        baseDatosMySQL.setPuerto(conf.getPuerto());
        baseDatosMySQL.setUsuario(conf.getUsuario());
        baseDatosMySQL.setPass(conf.getPass());
        MateriaDictadoConsultasMySQL materiaDictadoConsultasMySQL = new MateriaDictadoConsultasMySQL(baseDatosMySQL);
        MateriaDictadoPersistenteEnDisco materiaDictadoPersistenteEnDisco = new MateriaDictadoPersistenteEnDisco();
        Date fecha = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HHmmss");
        try {
            List<MateriaDictado> listaMateriasDictados = materiaDictadoConsultasMySQL.getAll();
            materiaDictadoPersistenteEnDisco.setListaMateriasDictados(listaMateriasDictados);
            materiaDictadoPersistenteEnDisco.setNombreArchivo("materiasDictadosBackup" + ".txt");
            materiaDictadoPersistenteEnDisco.guardar();
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        } catch (SQLException ex) {
            System.out.println(ex);
        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
        }
    }

    public static void guardarExamenes() {
        Configuracion conf = new Configuracion();
        BaseDatosMySQL baseDatosMySQL = new BaseDatosMySQL();
        baseDatosMySQL.setDireccionServer(conf.getDireccionServer());
        baseDatosMySQL.setNombreBaseDatos(conf.getNombreBaseDatos());
        baseDatosMySQL.setPuerto(conf.getPuerto());
        baseDatosMySQL.setUsuario(conf.getUsuario());
        baseDatosMySQL.setPass(conf.getPass());
        ExamenConsultasMySQL examenConsultasMySQL = new ExamenConsultasMySQL(baseDatosMySQL);
        ExamenPersistenteEnDisco examenPersistenteEnDisco = new ExamenPersistenteEnDisco();
        Date fecha = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HHmmss");
        try {
            List<Examen> listaExamenes = examenConsultasMySQL.getAll();
            examenPersistenteEnDisco.setListaExamenes(listaExamenes);
            examenPersistenteEnDisco.setNombreArchivo("examenesBackup" + ".txt");
            examenPersistenteEnDisco.guardar();
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        } catch (SQLException ex) {
            System.out.println(ex);
        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
        }
    }

    private static void guardarALumnoMateriasDictados() {
        try {
            Configuracion conf = new Configuracion();
            BaseDatosMySQL baseDatosMySQL = new BaseDatosMySQL();
            baseDatosMySQL.setDireccionServer(conf.getDireccionServer());
            baseDatosMySQL.setNombreBaseDatos(conf.getNombreBaseDatos());
            baseDatosMySQL.setPuerto(conf.getPuerto());
            baseDatosMySQL.setUsuario(conf.getUsuario());
            baseDatosMySQL.setPass(conf.getPass());
            AlumnoMateriaDictadoPersistenteEnDisco alumnoMateriaDictadoPersistenteEnDisco = new AlumnoMateriaDictadoPersistenteEnDisco(baseDatosMySQL);
            alumnoMateriaDictadoPersistenteEnDisco.setNombreArchivo("AlumnosMateriasDictadosBackup.txt");
            alumnoMateriaDictadoPersistenteEnDisco.guardar();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String args[]) {
        guardarAlumnos();
        guardarExamenes();
        guardarMaterias();
        guardarMateriasDictados();
        guardarProfesores();
        guardarALumnoMateriasDictados();
    }
}
