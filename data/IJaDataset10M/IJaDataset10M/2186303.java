package persistence.dataAccessObjects.interfaces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import exceptions.DatasetNotFoundException;
import persistence.transferPOJOs.Kommentar;
import persistence.transferPOJOs.produkte.Alkohol;
import persistence.transferPOJOs.produkte.Produkt;
import persistence.transferPOJOs.produkte.Utensil;
import persistence.transferPOJOs.produkte.Zigarre;

public interface IProduktDAO {

    public int insertAlkohol(Alkohol alkohol) throws ServletException;

    public int insertUtensil(Utensil utensil) throws ServletException;

    public int insertZigarre(Zigarre zigarre) throws ServletException;

    public boolean deleteProdukt(int id) throws DatasetNotFoundException, ServletException;

    public ArrayList<Produkt> getAllProductsGeneral() throws ServletException;

    public Produkt getProduktByID(int ID) throws ServletException, DatasetNotFoundException;

    public ArrayList<Produkt> getProduktByCategory(String category) throws ServletException, DatasetNotFoundException;

    public int insertKommentar(Kommentar kommentar) throws ServletException;

    public boolean changeProduktData(Produkt produkt) throws ServletException, DatasetNotFoundException;

    public boolean changeAlkoholData(Alkohol alkohol) throws ServletException, DatasetNotFoundException;

    public boolean changeUtensilData(Utensil utensil) throws ServletException, DatasetNotFoundException;

    public boolean changeZigarreData(Zigarre zigarre) throws ServletException, DatasetNotFoundException;

    public ArrayList<Kommentar> getKommentareByPID(int PID) throws ServletException;

    public ArrayList<Produkt> searchProducts(String searchterm) throws ServletException, DatasetNotFoundException;
}
