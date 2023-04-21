package clonmelServer;

import java.io.*;
import java.util.*;
import java.lang.*;

public class Shop implements Serializable {

    private ArrayList<Product> productArray;

    private ArrayList<Service> serviceArray;

    private ArrayList<Note> noteArray;

    private ArrayList<Product> orderArray;

    private ArrayList<Object> dataArray;

    private static BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

    private static String fileData = "./data.dat";

    public Shop() {
        productArray = new ArrayList<Product>();
        serviceArray = new ArrayList<Service>();
        noteArray = new ArrayList<Note>();
        orderArray = new ArrayList<Product>();
        dataArray = new ArrayList<Object>();
    }

    public void displayAllProducts() {
        int position = 1;
        for (Object obj : productArray) {
            Product p = (Product) obj;
            System.out.println(position);
            p.display();
            position++;
        }
    }

    public void displayProductTitles() {
        int position = 1;
        for (Object obj : productArray) {
            Product p = (Product) obj;
            System.out.println(position);
            p.getTitle();
            position++;
        }
    }

    public int readInt() {
        try {
            int i = Integer.parseInt(stdin.readLine());
            return i;
        } catch (Exception e) {
            System.err.println("Please enter a numeric value only!");
            int i = readInt();
            return i;
        }
    }

    public String readString() {
        try {
            return stdin.readLine();
        } catch (Exception e) {
            System.err.println("Please try again!");
            String str = readString();
            return str;
        }
    }

    public void addProduct() {
        System.out.println("You have now entered the add product menu.");
        System.out.println("To backup, please press 1, otherwise press 2.");
        boolean finished = false;
        int read = readInt();
        while (!finished) {
            if (read == 1) {
                System.out.println("You have returned to the main menu");
                System.out.println();
                finished = true;
            } else if (read == 2) {
                System.out.println("Please enter the following information after the prompt:");
                System.out.print("Title:		");
                String title = readString();
                System.out.print("Description:	");
                String description = readString();
                System.out.print("Stock:		");
                int stock = readInt();
                System.out.print("Cost:			");
                double cost = -1.0;
                boolean fin = false;
                while (!fin) {
                    try {
                        cost = Double.parseDouble(readString());
                        fin = true;
                    } catch (Exception e) {
                        System.out.println("Please enter a double value eg 0.00");
                        cost = 0.00;
                    }
                }
                System.out.print("On Order:		");
                boolean isOrdered = readString().toLowerCase().contentEquals("true");
                Product p = new Product(title, description, stock, cost, isOrdered);
                productArray.add(p);
                System.out.println("Successfully added");
                finished = true;
            } else System.out.println("You have made an invalid selection, please try again.");
        }
    }

    public void addNote() {
        Note n = new Note(readString());
    }

    public boolean writeFile() {
        boolean result = true;
        try {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileData));
            dataArray.set(0, productArray);
            dataArray.set(1, serviceArray);
            dataArray.set(2, noteArray);
            dataArray.set(3, orderArray);
            output.writeObject(dataArray);
        } catch (IOException ex) {
            System.out.println("IO exception has occurred :" + ex.getLocalizedMessage());
            result = false;
        } catch (Exception e) {
            System.out.println("System error has occurred :" + e.getLocalizedMessage());
            result = false;
        }
        return result;
    }

    public boolean readFile() {
        boolean result = true;
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(fileData));
            dataArray = (ArrayList) input.readObject();
            productArray = (ArrayList<Product>) dataArray.get(0);
            serviceArray = (ArrayList<Service>) dataArray.get(1);
            noteArray = (ArrayList<Note>) dataArray.get(2);
            orderArray = (ArrayList<Product>) dataArray.get(3);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("IO error has occurred");
            result = false;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unknown error has occurred");
            result = false;
        }
        return result;
    }

    public void consoleMenu() throws Exception {
        readFile();
        int choice = 1;
        while (choice != 0) {
            System.out.println("You are now in the main menu, please choose an option");
            System.out.println("\n\n\n****		Computer Centre Clonmel	\t****");
            System.out.println();
            System.out.println("* 1.\t	Print to file		\t****");
            System.out.println("* 2.\t	Add a product		\t****");
            System.out.println("* 3.\t	View all products	\t****");
            System.out.println("* 4.\t	View a product		\t****");
            System.out.println("* 5.\t	Edit a product		\t****");
            System.out.println("* 5.\t	Remove a product	\t****");
            System.out.println("*    \t				\t****");
            System.out.println("* 6.\t	Add a service		\t****");
            System.out.println("* 7.\t	View a service		\t****");
            System.out.println("* 8.\t	View all services	\t****");
            System.out.println("*   \t				\t****");
            System.out.println("* 9.\t	Create account		\t****");
            System.out.println("* 10.\t	View account		\t****");
            System.out.println("* 11.\t	Edit account	 	\t****");
            System.out.println("* 12.\t	Display All Accounts\t****");
            System.out.println("*    \t				\t****");
            System.out.println("* 13.\t	Write a note		\t****");
            System.out.println("* 14.\t	View all notes		****");
            System.out.println("* 15.\t	View a note			****");
            System.out.println("* 16.\t	Delete a note		\t****");
            System.out.println("* 17.\t	Make sale			\t****");
            System.out.println("* 5.\t	View weekly sales	\t****");
            System.out.println("* 18.\t	Orders				\t****");
            System.out.println("* 19.\t	Problems			\t****");
            System.out.println("* 0.\t            Save and exit	    \t****");
            System.out.println();
            System.out.println("****************************************");
            choice = readInt();
            if (choice == 1) {
                System.out.println("Please add your note");
                addNote();
            } else if (choice == 2) {
                addProduct();
            } else if (choice == -1) {
            }
        }
    }
}
