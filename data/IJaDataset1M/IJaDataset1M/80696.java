package p3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ClienteDirectorio {

    private static BufferedReader in;

    private static DirectorioInterface dir;

    public static void main(String args[]) {
        try {
            dir = (DirectorioInterface) Naming.lookup("rmi://localhost/servidor_directorio");
            System.out.println("Elija una opci�n:");
            System.out.println("1 - Buscar persona.");
            System.out.println("2 - Agregar persona.");
            System.out.println("3 - Eliminar persona.");
            System.out.println("4 - Modificar persona.");
            System.out.println("5 - Obtener directorio.");
            System.out.println("0 - Salir.");
            in = new BufferedReader(new InputStreamReader(System.in));
            boolean ft = true;
            while (true) {
                if (!ft) {
                    System.out.println("[1-Buscar|2-Agregar|3-Eliminar|4-Modificar|5-Guardar|0-Salir]");
                }
                ft = false;
                System.out.print("Opci�n? :");
                int op = Integer.parseInt(in.readLine());
                switch(op) {
                    case 1:
                        buscarPersona();
                        break;
                    case 2:
                        agregarPersona();
                        break;
                    case 3:
                        eliminarPersona();
                        break;
                    case 4:
                        modificarPersona();
                        break;
                    case 5:
                        mostrarDirectorio();
                        break;
                    case 0:
                        System.out.println("Adi�s!");
                        System.exit(0);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void eliminarPersona() throws IOException {
        System.out.print("Nombre de la persona: ");
        String nom = in.readLine();
        System.out.print("Apellido de la persona: ");
        String ape = in.readLine();
        try {
            dir.eliminarPersona(nom, ape);
            System.out.println("Persona eliminada.");
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void mostrarDirectorio() throws RemoteException {
        ArrayList dar = dir.obtenerDirectorio();
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream("directorio.txt"));
            for (int i = 0; i < dar.size(); i++) {
                String[] t = (String[]) dar.get(i);
                pw.println("{" + t[0] + "," + t[1] + "," + t[2] + "," + t[3] + "}");
            }
            pw.close();
            System.out.println("Se ha guardado un fichero 'directorio.txt' con la informaci�n de los contactos.");
        } catch (FileNotFoundException e) {
            System.err.println("No se ha podido guardar el directorio.");
        }
    }

    private static void modificarPersona() throws IOException {
        System.out.print("Nombre de la persona: ");
        String nom = in.readLine();
        System.out.print("Apellido de la persona: ");
        String ape = in.readLine();
        System.out.println("Modificar...");
        System.out.println("[1-Nombre|2-Apellido|3-Telefono|4-Calle|0-Salir]");
        System.out.print("Opcion? :");
        int op = Integer.parseInt(in.readLine());
        String nnom = "";
        String nape = "";
        String telf = "";
        String calle = "";
        switch(op) {
            case 1:
                System.out.print("Nuevo nombre: ");
                nnom = in.readLine();
                break;
            case 2:
                System.out.print("Nuevo apellido: ");
                nape = in.readLine();
                break;
            case 3:
                System.out.print("Nuevo telefono: ");
                telf = in.readLine();
                break;
            case 4:
                System.out.print("Nueva calle: ");
                calle = in.readLine();
                break;
            case 0:
                return;
        }
        try {
            dir.modificarUsuario(nom, ape, nnom, nape, telf, calle);
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void agregarPersona() throws IOException {
        System.out.print("Nombre de la persona: ");
        String nom = in.readLine();
        System.out.print("Apellido de la persona: ");
        String ape = in.readLine();
        try {
            dir.ingresarPersona(nom, ape);
            System.out.println("Se ha a�adido el contacto al directorio.");
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void buscarPersona() throws NumberFormatException, IOException {
        System.out.println("Buscar persona por...");
        System.out.println("[1-Nombre y apellido|2-Nombre|3-Apellido|0-Volver]");
        System.out.print("Opcion? :");
        int op = Integer.parseInt(in.readLine());
        String nom = "";
        String ape = "";
        switch(op) {
            case 1:
                System.out.print("Nombre de la persona: ");
                nom = in.readLine();
                System.out.print("Apellido de la persona: ");
                ape = in.readLine();
                try {
                    printPersona(dir.buscarTelefono(ape, nom));
                } catch (RemoteException e) {
                    System.err.println(e.getMessage());
                }
                break;
            case 2:
                System.out.print("Nombre de la persona: ");
                nom = in.readLine();
                try {
                    System.out.println("Resultado(s):");
                    printPersonas(dir.buscarTelefonoNombre(nom));
                } catch (RemoteException e) {
                    System.err.println(e.getMessage());
                }
                break;
            case 3:
                System.out.print("Apellido de la persona: ");
                ape = in.readLine();
                try {
                    System.out.println("Resultado(s):");
                    printPersonas(dir.buscarTelefonoApellido(ape));
                } catch (RemoteException e) {
                    System.err.println(e.getMessage());
                }
                break;
            case 0:
                System.out.println("Volvemos al men� principal...");
                return;
        }
    }

    private static void printPersonas(ArrayList list) {
        for (int i = 0; i < list.size(); i++) {
            printPersona((String[]) list.get(i));
        }
    }

    private static void printPersona(String[] p) {
        System.out.println(p[0] + " " + p[1] + " | " + p[2] + " | " + p[3]);
    }
}
