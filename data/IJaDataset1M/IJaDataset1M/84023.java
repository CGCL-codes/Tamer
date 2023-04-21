package br.com.visualmidia.update;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.prevayler.Transaction;
import br.com.visualmidia.GD;
import br.com.visualmidia.business.Account;
import br.com.visualmidia.business.BillCategory;
import br.com.visualmidia.business.Clause;
import br.com.visualmidia.business.GDDate;
import br.com.visualmidia.business.LabelPage;
import br.com.visualmidia.business.Money;
import br.com.visualmidia.business.Operation;
import br.com.visualmidia.business.Person;
import br.com.visualmidia.business.TypeOfPaper;
import br.com.visualmidia.core.Constants;
import br.com.visualmidia.persistence.GetAccounts;
import br.com.visualmidia.persistence.GetBillCategory;
import br.com.visualmidia.persistence.GetLabelPage;
import br.com.visualmidia.persistence.GetOperationByAccountId;
import br.com.visualmidia.persistence.GetPerson;
import br.com.visualmidia.persistence.GetProperty;
import br.com.visualmidia.persistence.GetTypeOfPaper;
import br.com.visualmidia.persistence.PrevalentSystem;
import br.com.visualmidia.persistence.RemoveAllClause;
import br.com.visualmidia.persistence.RemoveBillCategory;
import br.com.visualmidia.persistence.SetAmountOfAccount;
import br.com.visualmidia.persistence.SetProperty;
import br.com.visualmidia.persistence.UpdatePerson;
import br.com.visualmidia.persistence.UpdateRegistrationAppointment;
import br.com.visualmidia.persistence.add.AddBillCategory;
import br.com.visualmidia.persistence.add.AddCity;
import br.com.visualmidia.persistence.add.AddClause;
import br.com.visualmidia.persistence.add.AddLabelPage;
import br.com.visualmidia.persistence.add.AddPersonAndReturn;
import br.com.visualmidia.persistence.add.AddState;
import br.com.visualmidia.persistence.add.AddTypeOfPaper;
import br.com.visualmidia.persistence.query.GetClauses;
import br.com.visualmidia.persistence.remove.RemoveAllCities;
import br.com.visualmidia.persistence.remove.RemoveAllPresences;
import br.com.visualmidia.persistence.remove.RemoveAllStates;
import br.com.visualmidia.system.GDSystem;
import br.com.visualmidia.tools.Validator;

/**
 * @author  Lucas
 */
public class UpdateSetup {

    private GDSystem system;

    @SuppressWarnings("unchecked")
    public UpdateSetup() {
        this.system = GDSystem.getInstance();
        executeUpdate();
    }

    private void updatePrevayler() {
        try {
            system.stopPrevayler();
            File directory;
            if (GDSystem.isServerMode()) {
                directory = new File(Constants.PREVAYLER_SERVER_DATA_DIRETORY);
            } else {
                directory = new File(Constants.PREVAYLER_DATA_DIRETORY);
            }
            for (File file : directory.listFiles()) {
                if (file.getName().contains("transactionLog")) {
                    file.delete();
                }
            }
            system.startPrevayler();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getShortcutCreatorName() {
        if (GDSystem.isClientMode()) {
            return "createShortcutClient.vbs\" ";
        } else if (GDSystem.isServerMode()) {
            return "createShortcutServerClient.vbs\"";
        }
        return "createShortcut.vbs\"";
    }

    private void updateBirthdayDatePerson() {
        try {
            Map<String, Person> persons = (Map<String, Person>) system.query(new GetPerson());
            for (Person person : persons.values()) {
                if (person.getBirthDate().equals("  /  /    ")) {
                    GDDate gDate = new GDDate();
                    person.setBirthDate(gDate.getFormatedDate());
                    system.executeAndQuery(new AddPersonAndReturn(person));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertClausesOfContract() {
        addClause("1", "A primeira denominada CONTRATADA compromete-se a ministrar cursos de computa��o conforme o item \"Cursos Contratados\" para a segunda, denominada CONTRATANTE", true, true);
        addClause("2", "O local para realiza��o do curso ser� no endere�o: <Endere�oDaEmpresa>, em <CidadeDaEmpresa>, Estado de <EstadoDaEmpresa>, podendo ser transferido " + "para outro local na mesma cidade se assim for necess�rio.", true, false);
        addClause("3", "Pela remunera��o a CONTRATANTE ter� direito a apostilas referente aos cursos que a possu�rem, utiliza��o de computadores com supervisionamento adequado e Certificado de Conclus�o do(s) Curso(s) conclu�do(s) integralmente, conforme condi��es descritas neste contrato.", true, true);
        addClause("4", "O valor total do curso bem como seu parcelamento, valor da taxa de matr�cula e valor do desconto pontualidade a ser subtra�do para os pagamentos de parcelas efetuados at� sua respectiva data de vencimento est�o dispostas no item \"Valor do Contrato\".", true, false);
        addClause("5", "O valor individual das parcelas at� e ap�s seu vencimento, juntamente com as respectivas Datas de Vencimento est�o especificadas no item \"Mensalidade e Condi��es de Pagamento\".", true, false);
        addClause("6", "As parcelas dever�o ser quitadas at� o dia de seu vencimento mediante especifica��es do item \"Mensalidade e Condi��es de Pagamento\", onde ap�s ultrapassarem a data para quita��o sofrer�o a perda do valor estipulado como \"Desconto Pontualidade\" estipulado no item \"Valor do Contrato\".\n" + "Par�grafo �nico: Fica convencionado que os pagamentos em atraso ser�o acrescidos de multa de <Multa>%, mais juros di�rios de <JurosMora>%, sobre o valor da parcela.", true, false);
        addClause("7", "Ap�s 5 (cinco) meses de vig�ncia deste contrato, a cada reajuste do sal�rio m�nimo, o valor das parcelas ser�o  corrigidos atrav�s do mesmo �ndice utilizado pelo governo para reajuste do sal�rio M�nimo e ser� aplicado em todas as parcelas restantes a partir do pr�ximo m�s." + "\nPar�grafo 1�: Caso a infla��o acumulada no mesmo per�odo e apurada pelo IGP-M seja menor do que o reajuste salarial, ser� utilizado esse �ndice para corre��o das parcelas, ou seja, ser� dada prefer�ncia para o �ndice que resultar no " + "menor reajuste das parcelas, dentre as duas op��es expostas neste contrato." + "\nPar�grafo 2�: As mensalidades tamb�m poder�o ser corrigidas, caso haja grandes mudan�as " + "de car�ter econ�mico que influenciem a presta��o de servi�o ora contratada. Para tanto, ser� usado o �ndice que melhor represente o interesse de ambas as partes, evitando " + "que alguma delas possa ser lesada.", true, true);
        addClause("8", "A dura��o do curso em meses e sua carga hor�ria semanal est�o dispostas no item \"Identifica��o do Prazo, In�cio e Carga Hor�ria\"", true, false);
        addClause("9", "Se por algum motivo alheio aos termos estipulados nesse contrato, a CONTRATANTE antecipar a conclus�o do curso, terminando-o antes da data prevista, ficar� a mesma obrigada a pagar as parcelas restantes como forma de quita��o ao parcelamento assumido no item \"Valor do Contrato\", campo \"N�mero de Parcelas\".", true, true);
        addClause("10", "No caso de retardamento no curso, ou seja, caso a CONTRATANTE, precise continuar cursando mesmo ap�s o prazo deste contrato, o mesmo ser� automaticamente estendido at� o tempo necess�rio para a conclus�o dos cursos contratados. Neste caso a CONTRATANTE, pagar� parcelas at� a conclus�o dos cursos faltantes no valor igual ao das parcelas dispostas no item \"Mensalidades e Condi��es de Pagamentos\" neste contrato.", true, false);
        addClause("11", "A CONTRATADA reserva-se ano direito de entregar a primeira apostila a CONTRATANTE somente ap�s o pagamento da primeira parcela que deu origem ao presente contrato.\n" + "Par�grafo �nico: Em caso de cancelamento do contrato, s� ser�o de posse do CONTRATANTE as apostilas referentes aos cursos j� conclu�dos.", true, true);
        addClause("12", "A CONTRATANTE assina neste ato uma Nota Promiss�ria em favor da CONTRATADA, no valor de 2 (duas) parcelas, como garantia do cumprimento da obriga��o ora assumida.", true, true);
        addClause("13", "A CONTRATANTE somente ter� direito ao Certificado de Conclus�o contendo os cursos que tenha conclu�do integralmente e passado por todas as aulas e seus respectivos testes de avalia��o, freq�entando pelo menos 75% das aulas contratadas.", true, true);
        addClause("14", "N�o podem ser canceladas parcelas j� vencidas ou haver restitui��o de import�ncias pagas � CONTRATADA no caso de desist�ncia ou aus�ncia da CONTRATANTE.", true, true);
        addClause("15", "O presente CONTRATO s� poder� ser rescindido pela CONTRATANTE, mediante notifica��o � CONTRATADA, atrav�s de requerimento assinado e protocolado na sede da CONTRATADA." + "\nPar�grafo 1�: A parte que rescindir unilateralmente o presente contrato pagar� a outra, a t�tulo de multa, o valor correspondente a <ParcelasRecisao> parcelas." + "\nPar�grafo 2�: Para efetuar o " + "cancelamento, a CONTRATANTE deve ter quitado todas as parcelas vencidas, inclusive acrescidas de multa por atraso e juros de mora, quando necess�rio, mais uma parcela proporcional ao per�odo " + "compreendido entre a data da �ltima parcela vencida e a data do cancelamento do contrato." + "\nPar�grafo 3�: Fica estipulado que com o n�o pagamento de 2(duas) parcelas consecutivas, independente " + "do comparecimento ou n�o �s aulas, este contrato ser� automaticamente cancelado, ficando a CONTRATANTE ainda obrigada a cumprir o estipulado nos par�grafos 1� e 2� desta cl�usula e tendo seu nome " + "credenciado no Servi�o de Prote��o ao Cr�dito." + "\nPar�grafo 4�: No t�rmino do prazo estabelecido pelo presente contrato, ou em caso da rescis�o antecipada, uma vez cumprida a obriga��o da multa pela " + "CONTRATANTE, a CONTRATADA, se obriga a devolver a Nota Promiss�ria dada em garantia do cumprimento do presente contrato.", true, true);
        addClause("16", "Os custos provenientes da utiliza��o de instrumentos para a cobran�a de valores devidos ser�o de responsabilidade da CONTRATANTE.", true, true);
        addClause("17", "A CONTRATANTE, nesta data, afirma que tem plenas condi��es financeiras para arcar com todas as parcelas do presente contrato.", true, true);
        addClause("18", "A CONTRATANTE poder� transferir sua vaga a uma pessoa id�nea que dever� assinar um novo contrato junto a CONTRATADA. Neste caso, a CONTRATANTE ser� isenta da multa estipulada por rescis�o descrita neste CONTRATO, obrigando-se a quitar as parcelas vencidas at� a transfer�ncia.", true, true);
        addClause("19", "As diverg�ncias oriundas do presente contrato ser�o dirimidas pelas partes perante o Ju�zo da Comarca de <Localiza��o>.", true, false);
    }

    private void removeAllClauses() {
        system.execute(new RemoveAllClause());
    }

    public void removeOldClausesOfContractAndAddNewClauses() {
        try {
            Clause checkThisClause = (Clause) system.query(new GetClauses("4"));
            if (checkThisClause == null || !"O valor total do curso bem como seu parcelamento, valor da taxa de matr�cula e valor do desconto pontualidade a ser subtra�do para os pagamentos de parcelas efetuados at� sua respectiva data de vencimento est�o dispostas no item \"Valor do Contrato\".".equals(checkThisClause.getClause())) {
                System.out.println("Atualizando Clausulas de Contrato...");
                removeAllClauses();
                insertClausesOfContract();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addClause(String id, String clause, boolean isActive, boolean canBeRemoved) {
        Clause clausePersistence = new Clause(id, clause, isActive, canBeRemoved);
        system.execute(new AddClause(clausePersistence));
    }

    private void updateRegistrationAppointment() {
        system.execute(new UpdateRegistrationAppointment());
    }

    private void updateAmountAccount() {
        try {
            Map<String, Account> accounts = (Map<String, Account>) system.query(new GetAccounts());
            for (Account account : accounts.values()) {
                Money money = new Money(0);
                Map<String, Operation> operations = (Map<String, Operation>) system.query(new GetOperationByAccountId(account.getId()));
                for (Operation operation : operations.values()) {
                    if (operation.isCredit()) {
                        money.credit(operation.getValue());
                    } else {
                        money.debit(operation.getValue());
                    }
                }
                system.execute(new SetAmountOfAccount(money, account.getId()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void updateBornDateNotValid() {
        try {
            Map<String, Person> personMap = (Map<String, Person>) system.query(new GetPerson());
            for (Person person : personMap.values()) {
                if (!Validator.isDate(person.getBirthDate())) {
                    person.setBirthDate("01/01/1900");
                    system.executeAndQuery(new UpdatePerson(person));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertState() {
        try {
            system.execute(new AddState("AC", "Acre"));
            system.execute(new AddState("AL", "Alagoas"));
            system.execute(new AddState("AM", "Amazonas"));
            system.execute(new AddState("AP", "Amap�"));
            system.execute(new AddState("BA", "Bahia"));
            system.execute(new AddState("CE", "Cear�"));
            system.execute(new AddState("DF", "Distrito Federal"));
            system.execute(new AddState("ES", "Espirito Santo"));
            system.execute(new AddState("GO", "Goi�s"));
            system.execute(new AddState("MA", "Maranh�o"));
            system.execute(new AddState("MG", "Minas Gerais"));
            system.execute(new AddState("MS", "Mato Grosso do Sul"));
            system.execute(new AddState("MT", "Mato Grosso"));
            system.execute(new AddState("PA", "Par�"));
            system.execute(new AddState("PB", "Para�ba"));
            system.execute(new AddState("PE", "Pernambuco"));
            system.execute(new AddState("PI", "Piau�"));
            system.execute(new AddState("PR", "Paran�"));
            system.execute(new AddState("RJ", "Rio de Janeiro"));
            system.execute(new AddState("RN", "Rio Grande do Norte"));
            system.execute(new AddState("RO", "Rondonia"));
            system.execute(new AddState("RR", "Roraima"));
            system.execute(new AddState("RS", "Rio Grande do Sul"));
            system.execute(new AddState("SC", "Santa Catarina"));
            system.execute(new AddState("SE", "Sergipe"));
            system.execute(new AddState("SP", "S�o Paulo"));
            system.execute(new AddState("TO", "Tocantins"));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void updateStatesAndCities() {
        if (new File(Constants.CURRENT_DIR + "Municipios.txt").exists()) {
            clearCitiesAndStatesBeforeInsert();
            insertState();
            insertCities();
        }
    }

    private void clearCitiesAndStatesBeforeInsert() {
        system.execute(new RemoveAllCities());
        system.execute(new RemoveAllStates());
    }

    @SuppressWarnings("unchecked")
    private void insertCities() {
        try {
            List<String> list = fileToList(Constants.CURRENT_DIR + "Municipios.txt");
            String state;
            String city;
            for (int i = 0; i < list.size(); i++) {
                String cityItemTemp = list.get(i);
                state = cityItemTemp.substring(0, 2);
                city = cityItemTemp.substring(7);
                system.execute(new AddCity(city, state));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> fileToList(String fileName) {
        List<String> listTest = new ArrayList<String>();
        String inputLine;
        try {
            File inFile = new File(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));
            while ((inputLine = br.readLine()) != null) {
                listTest.add(inputLine.trim());
            }
            br.close();
            inFile.delete();
        } catch (FileNotFoundException ex) {
            System.out.println("File Not Found Exception: InputFileDeclared");
        } catch (IOException ex) {
            System.out.println("IOException: InputFileDeclared");
        }
        return listTest;
    }

    @SuppressWarnings("unchecked")
    private void executeUpdate() {
        insertgracedays();
        removeOldClausesOfContractAndAddNewClauses();
        try {
            File file = new File(Constants.CURRENT_DIR + "update.gd");
            if (file.exists()) {
                updatePrevayler();
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** @author Filipe B. Gallo 	
     *  @since	Data: 21/11/07 15:00
     *  @see C�digo inserido para verificar se o prazo de car�ncia j� existe na persist�ncia caso n�o exista 
     *  ser� criado o campo e inserido o prazo de 0 dias
     */
    @SuppressWarnings("unchecked")
    private void insertgracedays() {
        if (system.getProperty("gracedays").toString().equals("")) {
            Map<String, Object> properties;
            try {
                properties = (Map<String, Object>) system.query(new GetProperty());
                properties.put("gracedays", "0");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void deletePresences() {
        system.execute(new RemoveAllPresences());
    }

    private void updateBillPlan(String parentDescription, String description) {
        try {
            BillCategory bill = (BillCategory) system.query(new GetBillCategory(parentDescription));
            int i = 0;
            for (BillCategory b : bill.getAll()) {
                if (b.getDescription().equals(description)) {
                    i++;
                }
            }
            for (int j = 0; j < i; j++) {
                system.execute(new RemoveBillCategory(parentDescription, description));
            }
            system.execute(new AddBillCategory(parentDescription, description));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void insertTypeOfPaper() {
        try {
            Map<String, TypeOfPaper> typeOfPaperMap = (Map<String, TypeOfPaper>) system.query(new GetTypeOfPaper());
            if (typeOfPaperMap.size() == 0) {
                system.executeAndQuery(new AddTypeOfPaper("A4", 210, 293));
                system.executeAndQuery(new AddTypeOfPaper("Carta", 215.9f, 279.4f));
                system.executeAndQuery(new AddTypeOfPaper("Of�cio", 215.9f, 355.6f));
                system.executeAndQuery(new AddTypeOfPaper("Executivo", 184.1f, 266.7f));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void insertLabelMailing() {
        try {
            Map<String, LabelPage> labelPageMap = (Map<String, LabelPage>) system.query(new GetLabelPage());
            TypeOfPaper typeLetter = (TypeOfPaper) system.query(new GetTypeOfPaper("Carta"));
            TypeOfPaper typeA4 = (TypeOfPaper) system.query(new GetTypeOfPaper("A4"));
            if (labelPageMap.size() == 0) {
                system.executeAndQuery(new AddLabelPage("5580A", 66.7f, 25.4f, 3.1f, 0, 4.8f, 4.8f, 12.7f, 12.7f, 10, 3, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("5580M", 66.7f, 25.4f, 3.1f, 0, 4.8f, 4.8f, 12.7f, 12.7f, 10, 3, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("5580V", 66.7f, 25.4f, 3.1f, 0, 4.8f, 4.8f, 12.7f, 12.7f, 10, 3, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("6080", 66.7f, 25.4f, 3.1f, 0, 4.8f, 4.8f, 12.7f, 12.7f, 10, 3, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("6180", 66.7f, 25.4f, 3.1f, 0, 4.8f, 4.8f, 12.7f, 12.7f, 10, 3, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("6280", 66.7f, 25.4f, 3.1f, 0, 4.8f, 4.8f, 12.7f, 12.7f, 10, 3, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("62580", 66.7f, 25.4f, 3.1f, 0, 4.8f, 4.8f, 12.7f, 12.7f, 10, 3, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("6081", 101.6f, 25.4f, 5.2f, 0, 3.77f, 3.77f, 12.7f, 12.7f, 10, 2, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("6181", 101.6f, 25.4f, 5.2f, 0, 3.77f, 3.77f, 12.7f, 12.7f, 10, 2, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("6281", 101.6f, 25.4f, 5.2f, 0, 3.77f, 3.77f, 12.7f, 12.7f, 10, 2, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("62581", 101.6f, 25.4f, 5.2f, 0, 3.77f, 3.77f, 12.7f, 12.7f, 10, 2, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("6082", 101.6f, 33.9f, 5.16f, 0, 3.77f, 3.77f, 21.2f, 21.2f, 7, 2, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("6182", 101.6f, 33.9f, 5.16f, 0, 3.77f, 3.77f, 21.2f, 21.2f, 7, 2, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("6282", 101.6f, 33.9f, 5.16f, 0, 3.77f, 3.77f, 21.2f, 21.2f, 7, 2, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("62582", 101.6f, 33.9f, 5.16f, 0, 3.77f, 3.77f, 21.2f, 21.2f, 7, 2, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("6083", 101.6f, 50.8f, 5.16f, 0, 3.77f, 3.77f, 12.7f, 12.7f, 5, 2, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("6183", 101.6f, 50.8f, 5.16f, 0, 3.77f, 3.77f, 12.7f, 12.7f, 5, 2, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("6283", 101.6f, 50.8f, 5.16f, 0, 3.77f, 3.77f, 12.7f, 12.7f, 5, 2, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("6084", 101.6f, 84.7f, 5.16f, 0, 3.77f, 3.77f, 12.7f, 12.7f, 3, 2, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("6184", 101.6f, 84.7f, 5.16f, 0, 3.77f, 3.77f, 12.7f, 12.7f, 3, 2, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("6284", 101.6f, 84.7f, 5.16f, 0, 3.77f, 3.77f, 12.7f, 12.7f, 3, 2, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("6085", 215.9f, 279.4f, 0, 0, 0, 0, 0, 0, 1, 1, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("6185", 215.9f, 279.4f, 0, 0, 0, 0, 0, 0, 1, 1, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("6285", 215.9f, 279.4f, 0, 0, 0, 0, 0, 0, 1, 1, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("6086", 212.7f, 138.1f, 0, 0, 1.6f, 1.6f, 1.6f, 1.6f, 2, 1, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("6286", 212.7f, 138.1f, 0, 0, 1.6f, 1.6f, 1.6f, 1.6f, 2, 1, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("6087", 44.4f, 12.7f, 3.1f, 0, 14.5f, 14.5f, 12.7f, 12.7f, 20, 4, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("6187", 44.4f, 12.7f, 3.1f, 0, 14.5f, 14.5f, 12.7f, 12.7f, 20, 4, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("6287", 44.4f, 12.7f, 3.1f, 0, 14.5f, 14.5f, 12.7f, 12.7f, 20, 4, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("6088", 106.36f, 138.1f, 0, 0, 1.58f, 1.58f, 1.58f, 1.58f, 2, 2, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("6288", 106.36f, 138.1f, 0, 0, 1.58f, 1.58f, 1.58f, 1.58f, 2, 2, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("6089", 44.45f, 16.93f, 3.05f, 0, 14.5f, 14.5f, 12.7f, 12.7f, 15, 4, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("6092", 25.4f, 25.4f, 3.531f, 3.175f, 8.382f, 8.382f, 12.497f, 12.497f, 9, 7, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("6093", 42.33f, 42.33f, 10.211f, 1.981f, 7.976f, 7.976f, 7.976f, 7.976f, 6, 4, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("6094", 63.5f, 63.5f, 4.496f, 2.007f, 8.204f, 8.204f, 9.703f, 9.703f, 4, 3, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("6095", 85.7f, 59.3f, 10.6f, 0, 17.0f, 17.0f, 21.2f, 21.2f, 4, 2, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("7088", 88.9f, 50.8f, 0, 0, 19.0f, 19.0f, 12.7f, 12.7f, 5, 2, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("7188", 88.9f, 50.8f, 0, 0, 19.0f, 19.0f, 12.7f, 12.7f, 5, 2, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("8096", 69.8f, 69.8f, 0, 6.4f, 3.2f, 3.2f, 12.7f, 12.7f, 3, 3, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("8196", 69.8f, 69.8f, 0, 6.4f, 3.2f, 3.2f, 12.7f, 12.7f, 3, 3, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("8296", 69.8f, 69.8f, 0, 6.4f, 3.2f, 3.2f, 12.7f, 12.7f, 3, 3, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("8098", 88.9f, 42.3f, 12.7f, 0, 12.7f, 12.7f, 12.7f, 12.7f, 6, 2, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("8099F", 77.8f, 46.6f, 6, 0, 27.2f, 27.2f, 23.3f, 23.3f, 5, 2, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("8099L", 147.6f, 16.9f, 0, 0, 34.1f, 34.1f, 12.7f, 12.7f, 15, 1, typeLetter, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4248", 31, 17, 2, 0, 7, 7, 12.5f, 12.5f, 16, 6, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4348", 31, 17, 2, 0, 7, 7, 12.5f, 12.5f, 16, 6, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4249", 26, 15, 2, 0, 8, 8, 13.5f, 13.5f, 18, 7, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4349", 26, 15, 2, 0, 8, 8, 13.5f, 13.5f, 18, 7, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4250", 99, 55.8f, 2.6f, 0, 4.7f, 4.7f, 9, 9, 5, 2, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4350", 99, 55.8f, 2.6f, 0, 4.7f, 4.7f, 9, 9, 5, 2, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4251", 38.2f, 21.2f, 2.5f, 0, 4.5f, 4.5f, 10.7f, 10.7f, 13, 5, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4351", 38.2f, 21.2f, 2.5f, 0, 4.5f, 4.5f, 10.7f, 10.7f, 13, 5, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4254", 99, 25.4f, 2.6f, 0, 4.7f, 4.7f, 8.8f, 8.8f, 11, 2, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4354", 99, 25.4f, 2.6f, 0, 4.7f, 4.7f, 8.8f, 8.8f, 11, 2, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4255", 63.5f, 31, 2.6f, 0, 7.15f, 7.15f, 9, 9, 9, 3, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4355", 63.5f, 31, 2.6f, 0, 7.15f, 7.15f, 9, 9, 9, 3, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4256", 63.5f, 25.4f, 2.6f, 0, 7.15f, 7.15f, 8.8f, 8.8f, 11, 3, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4356", 63.5f, 25.4f, 2.6f, 0, 7.15f, 7.15f, 8.8f, 8.8f, 11, 3, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4260", 63.5f, 38.1f, 2.6f, 0, 7.15f, 7.15f, 15.15f, 15.15f, 7, 3, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4360", 63.5f, 38.1f, 2.6f, 0, 7.15f, 7.15f, 15.15f, 15.15f, 7, 3, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4261", 63.5f, 46.47f, 2.6f, 0, 7.15f, 7.15f, 9.09f, 9.09f, 6, 3, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4361", 63.5f, 46.47f, 2.6f, 0, 7.15f, 7.15f, 9.09f, 9.09f, 6, 3, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4262", 99, 33.9f, 2.6f, 0, 4.7f, 4.7f, 12.9f, 12.9f, 8, 2, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4362", 99, 33.9f, 2.6f, 0, 4.7f, 4.7f, 12.9f, 12.9f, 8, 2, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4263", 99, 38.1f, 2.6f, 0, 4.7f, 4.7f, 15.15f, 15.15f, 7, 2, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4363", 99, 38.1f, 2.6f, 0, 4.7f, 4.7f, 15.15f, 15.15f, 7, 2, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4264", 63.5f, 71.92f, 2.6f, 0, 7.15f, 7.15f, 4.66f, 4.66f, 4, 3, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4364", 63.5f, 71.92f, 2.6f, 0, 7.15f, 7.15f, 4.66f, 4.66f, 4, 3, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4265", 99, 67.8f, 2.6f, 0, 4.7f, 4.7f, 12.96f, 12.96f, 4, 2, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4365", 99, 67.8f, 2.6f, 0, 4.7f, 4.7f, 12.96f, 12.96f, 4, 2, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4266", 99, 93, 2.6f, 0, 4.7f, 4.7f, 9, 9, 3, 2, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4366", 99, 93, 2.6f, 0, 4.7f, 4.7f, 9, 9, 3, 2, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4267", 200, 288.5f, 0, 0, 5, 5, 4.25f, 4.25f, 1, 1, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4367", 200, 288.5f, 0, 0, 5, 5, 4.25f, 4.25f, 1, 1, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4268", 199.9f, 143.4f, 0, 0, 5.05f, 5.05f, 5.1f, 5.1f, 2, 1, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4368", 199.9f, 143.4f, 0, 0, 5.05f, 5.05f, 5.1f, 5.1f, 2, 1, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4291F", 76.2f, 46.4f, 2.5f, 0, 27.55f, 27.55f, 9.23f, 9.23f, 6, 2, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4291L", 145, 17, 0, 0, 32.25f, 32.25f, 12.43f, 12.43f, 16, 1, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4292", 70, 52, 23.3f, 0, 23.35f, 23.35f, 18.43f, 18.43f, 5, 2, typeA4, "Pimaco"));
                system.executeAndQuery(new AddLabelPage("A4293", 42, 42, 4, 2, 15, 15, 17.42f, 17.42f, 6, 4, typeA4, "Pimaco"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
