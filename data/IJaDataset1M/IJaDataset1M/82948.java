package com.miden2ena.mogeci.pojo;

import junit.framework.*;
import java.util.Date;

/**
 *
 * @author dbarison
 */
public class PersonaTest extends TestCase {

    Persona p;

    public PersonaTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        p = new Persona();
    }

    /**
     * Test of getCurriculumVitae method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testGetCurriculumVitae() {
        System.out.println("getCurriculumVitae");
        Persona instance = new Persona();
        CurriculumVitae expResult = null;
        CurriculumVitae result = instance.getCurriculumVitae();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCurriculumVitae method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testSetCurriculumVitae() {
        System.out.println("setCurriculumVitae");
        Persona instance = new Persona();
        CurriculumVitae cv = null;
        instance.setCurriculumVitae(cv);
        CurriculumVitae result = instance.getCurriculumVitae();
        assertEquals(null, result);
    }

    /**
     * Test of getNome method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testGetNome() {
        System.out.println("getNome");
        Persona instance = new Persona();
        String expResult = null;
        String result = instance.getNome();
        assertEquals(expResult, result);
    }

    /**
     * Test of setNome method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testSetNome() {
        System.out.println("setNome");
        String nome = "test";
        Persona instance = new Persona();
        instance.setNome(nome);
        String result = instance.getNome();
        assertEquals("test", result);
    }

    /**
     * Test of getCognome method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testGetCognome() {
        System.out.println("getCognome");
        Persona instance = new Persona();
        String expResult = null;
        String result = instance.getCognome();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCognome method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testSetCognome() {
        System.out.println("setCognome");
        String cognome = "test";
        Persona instance = new Persona();
        instance.setCognome(cognome);
        String result = instance.getCognome();
        assertEquals("test", result);
    }

    /**
     * Test of getDataNascita method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testGetDataNascita() {
        System.out.println("getDataNascita");
        Persona instance = new Persona();
        Date expResult = null;
        Date result = instance.getDataNascita();
        assertEquals(expResult, result);
    }

    /**
     * Test of setDataNascita method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testSetDataNascita() {
        System.out.println("setDataNascita");
        Date dataNascita = null;
        Persona instance = new Persona();
        instance.setDataNascita(dataNascita);
        Date result = instance.getDataNascita();
        assertEquals(null, result);
    }

    /**
     * Test of getLuogoNascita method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testGetLuogoNascita() {
        System.out.println("getLuogoNascita");
        Persona instance = new Persona();
        String expResult = null;
        String result = instance.getLuogoNascita();
        assertEquals(expResult, result);
    }

    /**
     * Test of setLuogoNascita method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testSetLuogoNascita() {
        System.out.println("setLuogoNascita");
        String luogoNascita = "test";
        Persona instance = new Persona();
        instance.setLuogoNascita(luogoNascita);
        String result = instance.getLuogoNascita();
        assertEquals("test", result);
    }

    /**
     * Test of getNazionalita method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testGetNazionalita() {
        System.out.println("getNazionalita");
        Persona instance = new Persona();
        String expResult = null;
        String result = instance.getNazionalita();
        assertEquals(expResult, result);
    }

    /**
     * Test of setNazionalita method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testSetNazionalita() {
        System.out.println("setNazionalita");
        String nazionalita = "test";
        Persona instance = new Persona();
        instance.setNazionalita(nazionalita);
        String result = instance.getNazionalita();
        assertEquals("test", result);
    }

    /**
     * Test of getSesso method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testGetSesso() {
        System.out.println("getSesso");
        Persona instance = new Persona();
        char expResult = 'M';
        char result = instance.getSesso();
        assertEquals(expResult, result);
    }

    /**
     * Test of setSesso method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testSetSesso() {
        System.out.println("setSesso");
        char sesso = 'm';
        Persona instance = new Persona();
        instance.setSesso(sesso);
        char result = instance.getSesso();
        assertEquals('m', result);
    }

    /**
     * Test of getCodFiscale method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testGetCodFiscale() {
        System.out.println("getCodFiscale");
        Persona instance = new Persona();
        long expResult = 0L;
        long result = instance.getCodFiscale();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCodFiscale method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testSetCodFiscale() {
        System.out.println("setCodFiscale");
        long codFiscale = 0L;
        Persona instance = new Persona();
        instance.setCodFiscale(codFiscale);
        long result = instance.getCodFiscale();
        assertEquals(0L, result);
    }

    /**
     * Test of getIndirizzoResidenza method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testGetIndirizzoResidenza() {
        System.out.println("getIndirizzoResidenza");
        Persona instance = new Persona();
        String expResult = null;
        String result = instance.getIndirizzoResidenza();
        assertEquals(expResult, result);
    }

    /**
     * Test of setIndirizzoResidenza method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testSetIndirizzoResidenza() {
        System.out.println("setIndirizzoResidenza");
        String indirizzoResidenza = "test";
        Persona instance = new Persona();
        instance.setIndirizzoResidenza(indirizzoResidenza);
        String result = instance.getIndirizzoResidenza();
        assertEquals("test", result);
    }

    /**
     * Test of getLuogoResidenza method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testGetLuogoResidenza() {
        System.out.println("getLuogoResidenza");
        Persona instance = new Persona();
        String expResult = null;
        String result = instance.getLuogoResidenza();
        assertEquals(expResult, result);
    }

    /**
     * Test of setLuogoResidenza method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testSetLuogoResidenza() {
        System.out.println("setLuogoResidenza");
        String luogoResidenza = "test";
        Persona instance = new Persona();
        instance.setLuogoResidenza(luogoResidenza);
        String result = instance.getLuogoResidenza();
        assertEquals("test", result);
    }

    /**
     * Test of getCapResidenza method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testGetCapResidenza() {
        System.out.println("getCapResidenza");
        Persona instance = new Persona();
        int expResult = 0;
        int result = instance.getCapResidenza();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCapResidenza method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testSetCapResidenza() {
        System.out.println("setCapResidenza");
        int capResidenza = 0;
        Persona instance = new Persona();
        instance.setCapResidenza(capResidenza);
        int result = instance.getCapResidenza();
        assertEquals(0, result);
    }

    /**
     * Test of getIndirizzoRecapito method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testGetIndirizzoRecapito() {
        System.out.println("getIndirizzoRecapito");
        Persona instance = new Persona();
        String expResult = null;
        String result = instance.getIndirizzoRecapito();
        assertEquals(expResult, result);
    }

    /**
     * Test of setIndirizzoRecapito method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testSetIndirizzoRecapito() {
        System.out.println("setIndirizzoRecapito");
        String indirizzoRecapito = "test";
        Persona instance = new Persona();
        instance.setIndirizzoRecapito(indirizzoRecapito);
        String result = instance.getIndirizzoRecapito();
        assertEquals("test", result);
    }

    /**
     * Test of getLuogoRecapito method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testGetLuogoRecapito() {
        System.out.println("getLuogoRecapito");
        Persona instance = new Persona();
        String expResult = null;
        String result = instance.getLuogoRecapito();
        assertEquals(expResult, result);
    }

    /**
     * Test of setLuogoRecapito method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testSetLuogoRecapito() {
        System.out.println("setLuogoRecapito");
        String luogoRecapito = "test";
        Persona instance = new Persona();
        instance.setLuogoRecapito(luogoRecapito);
        String result = instance.getLuogoRecapito();
        assertEquals("test", result);
    }

    /**
     * Test of getCapRecapito method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testGetCapRecapito() {
        System.out.println("getCapRecapito");
        Persona instance = new Persona();
        int expResult = 0;
        int result = instance.getCapRecapito();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCapRecapito method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testSetCapRecapito() {
        System.out.println("setCapRecapito");
        int capRecapito = 0;
        Persona instance = new Persona();
        instance.setCapRecapito(capRecapito);
        int result = instance.getCapRecapito();
        assertEquals(0, result);
    }

    /**
     * Test of getTelAbitazione method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testGetTelAbitazione() {
        System.out.println("getTelAbitazione");
        Persona instance = new Persona();
        long expResult = 0L;
        long result = instance.getTelAbitazione();
        assertEquals(expResult, result);
    }

    /**
     * Test of setTelAbitazione method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testSetTelAbitazione() {
        System.out.println("setTelAbitazione");
        long telAbitazione = 0L;
        Persona instance = new Persona();
        instance.setTelAbitazione(telAbitazione);
        long result = instance.getTelAbitazione();
        assertEquals(0L, result);
    }

    /**
     * Test of getTelCellulare method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testGetTelCellulare() {
        System.out.println("getTelCellulare");
        Persona instance = new Persona();
        long expResult = 0L;
        long result = instance.getTelCellulare();
        assertEquals(expResult, result);
    }

    /**
     * Test of setTelCellulare method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testSetTelCellulare() {
        System.out.println("setTelCellulare");
        long telCellulare = 0L;
        Persona instance = new Persona();
        instance.setTelCellulare(telCellulare);
        long result = instance.getTelCellulare();
        assertEquals(0L, result);
    }

    /**
     * Test of getStatoCivile method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testGetStatoCivile() {
        System.out.println("getStatoCivile");
        Persona instance = new Persona();
        String expResult = null;
        String result = instance.getStatoCivile();
        assertEquals(expResult, result);
    }

    /**
     * Test of setStatoCivile method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testSetStatoCivile() {
        System.out.println("setStatoCivile");
        String statoCivile = "test";
        Persona instance = new Persona();
        instance.setStatoCivile(statoCivile);
        String result = instance.getStatoCivile();
        assertEquals("test", result);
    }

    /**
     * Test of getId method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testGetId() {
        System.out.println("getId");
        Persona instance = new Persona();
        long expResult = 0L;
        long result = instance.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of setId method, of class com.miden2ena.mogeci.pojo.Persona.
     */
    public void testSetId() {
        System.out.println("setId");
        long id = 0L;
        Persona instance = new Persona();
        instance.setId(id);
        long result = instance.getId();
        assertEquals(0L, result);
    }
}
