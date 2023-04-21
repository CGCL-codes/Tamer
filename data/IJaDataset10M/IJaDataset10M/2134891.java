package com.calipso.reportgenerator.reportmanager.repository;

import com.calipso.reportgenerator.common.VersionProperties;
import com.calipso.reportgenerator.common.LanguageTraslator;
import com.calipso.reportgenerator.common.User;
import com.calipso.reportgenerator.common.exception.InfoException;
import java.util.*;
import java.io.*;

/**
 *
 * User: jbassino
 * Date: 10/03/2005
 * Time: 19:06:23
 *
 */
public class UserDataRepository {

    private HashMap users = new HashMap();

    private String repositoryPath;

    /**
   * Crea una instancia de <code>UsersRepository</code>
   * @param repositoryPath
   */
    public UserDataRepository(String repositoryPath) throws InfoException {
        try {
            this.repositoryPath = repositoryPath;
            initialize(new File(repositoryPath));
        } catch (IOException e) {
            throw new InfoException(LanguageTraslator.traslate("572"), e);
        }
    }

    /**
   * Llena un diccionario con los valores del repositorio
   * en caso de que este ultimo exista
   * @param repositoryFile
   * @throws java.io.IOException
   */
    private void initialize(File repositoryFile) throws IOException {
        if (repositoryFile.exists()) {
            fillUsersMapFrom(repositoryFile);
        }
    }

    /**
   * Llena un diccionario con los valores del repositorio.
   * Cada entrada al diccionario contiene el nombre de usuario
   * mientras que a cada entrada le corresponden las descripciones
   * de los usuarios.
   * @param repositoryFile
   */
    private void fillUsersMapFrom(File repositoryFile) {
        try {
            FileReader fileReader = new FileReader(repositoryFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                if (!line.equals("")) {
                    StringTokenizer tokenizer = new StringTokenizer(line, ";");
                    String userId = tokenizer.nextToken();
                    String userName = tokenizer.nextToken();
                    String userCompany = tokenizer.nextToken();
                    Vector data = new Vector(2);
                    data.add(userName);
                    data.add(userCompany);
                    getUsers().put(userId, data);
                }
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            fileReader.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    public Collection getUserData(String userId) {
        if (getUsers().containsKey(userId)) {
            return (Collection) getUsers().get(userId);
        }
        return null;
    }

    /**
   * Agrega un nuevo usuario al file Userdata (graba una linea de la forma :
   *  "111;Usu1;comp1"), antes validando que el usuario no fuese ya insertado y
   * que la cantidad de usuarios no supere a la permitida
   * @param userId
   * @param name
   * @param company
   * @throws InfoException
   */
    public void addNewUser(String userId, String name, String company) throws InfoException {
        try {
            if (getUsers().containsKey(userId)) {
                throw new InfoException(LanguageTraslator.traslate("394"));
            }
            File outputFile = new File(repositoryPath);
            FileWriter fileWriter = null;
            fileWriter = new FileWriter(outputFile.getPath(), true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(userId + ";" + name + ";" + company + '\n');
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            throw new InfoException(LanguageTraslator.traslate("477"), e);
        }
    }

    /**
   * Devuelve los usuarios del repositorio
   * @return mapa de usuarios (con la forma: 111->[usu1,comp1])
   */
    private HashMap getUsers() {
        if (users == null) {
            users = new HashMap();
        }
        return users;
    }

    /**
   * Devuelve una coleccion con todos
   * los  objetos usuario  (partiendo desde un mapa,
   * se forma un Set con todos los ususrios)
   * @return  set con todos los usuarios
   */
    public Set getAllUsers() {
        Iterator it = users.entrySet().iterator();
        Set listNames = new TreeSet();
        while (it.hasNext()) {
            User us;
            Map.Entry list = (Map.Entry) it.next();
            Vector list1 = new Vector();
            list1.addAll((Collection) list.getValue());
            us = new User((String) list.getKey(), (String) list1.get(0), (String) list1.get(1));
            listNames.add(us);
        }
        return listNames;
    }

    /**
   * Elimina un usuario de la  aplicaci n (del mapa de usuarios y
   * del file userData )
   * @param user
   * @throws InfoException
   */
    public void removeUser(String user) throws InfoException {
        users.remove(user);
        this.removeUserToFileDataUser();
    }

    /**
   *  Carga nuevamente el file UserData,
   *  con el mapa users ya actualizado
   * @throws InfoException
   */
    private void removeUserToFileDataUser() throws InfoException {
        try {
            Iterator it = users.entrySet().iterator();
            File outputFile = new File(repositoryPath);
            FileWriter fileWriter = null;
            fileWriter = new FileWriter(outputFile.getPath());
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            while (it.hasNext()) {
                Map.Entry list = (Map.Entry) it.next();
                String idUser = (String) list.getKey();
                String userString = idUser;
                Vector vector = (Vector) list.getValue();
                String string = "";
                for (int i = 0; i < vector.size(); i++) {
                    string += ";";
                    String s = (String) vector.elementAt(i);
                    string += s;
                }
                bufferedWriter.write(userString + string + "\n");
            }
            bufferedWriter.flush();
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            throw new InfoException(LanguageTraslator.traslate("478"), e);
        }
    }
}
