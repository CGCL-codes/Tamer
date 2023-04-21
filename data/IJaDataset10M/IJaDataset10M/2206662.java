package br.com.cinepointer.datatypes;

import java.util.ArrayList;
import java.util.HashMap;

public class Variables {

    private HashMap<Integer, HashMap<String, Integer>> citiesInBrasil;

    private HashMap<String, Integer> states;

    private HashMap<String, String> genres;

    static Variables instance = null;

    public static Variables getInstance() {
        if (instance == null) {
            instance = new Variables();
            return instance;
        } else {
            return instance;
        }
    }

    private Variables() {
        citiesInBrasil = new HashMap<Integer, HashMap<String, Integer>>();
        citiesInBrasil.put(0, new HashMap<String, Integer>());
        citiesInBrasil.get(0).put("Todas Cidades", 0);
        citiesInBrasil.put(1, new HashMap<String, Integer>());
        citiesInBrasil.get(1).put("Maceió", 79);
        citiesInBrasil.put(2, new HashMap<String, Integer>());
        citiesInBrasil.get(2).put("Manaus", 80);
        citiesInBrasil.put(3, new HashMap<String, Integer>());
        citiesInBrasil.get(3).put("Salvador", 84);
        citiesInBrasil.put(4, new HashMap<String, Integer>());
        citiesInBrasil.get(4).put("Fortaleza", 86);
        citiesInBrasil.put(5, new HashMap<String, Integer>());
        citiesInBrasil.get(5).put("Brasília", 87);
        citiesInBrasil.get(5).put("Taguatinga", 883);
        citiesInBrasil.put(6, new HashMap<String, Integer>());
        citiesInBrasil.get(6).put("Vila Velha", 91);
        citiesInBrasil.get(6).put("Vitória", 92);
        citiesInBrasil.put(7, new HashMap<String, Integer>());
        citiesInBrasil.get(7).put("Brasília - Aguas Claras", 934);
        citiesInBrasil.get(7).put("Goiânia", 94);
        citiesInBrasil.put(8, new HashMap<String, Integer>());
        citiesInBrasil.get(8).put("Campo Grande", 106);
        citiesInBrasil.get(8).put("Dourados", 1037);
        citiesInBrasil.put(9, new HashMap<String, Integer>());
        citiesInBrasil.get(9).put("Belo Horizonte", 95);
        citiesInBrasil.get(9).put("Poços de Caldas", 102);
        citiesInBrasil.get(9).put(" Uberlândia", 105);
        citiesInBrasil.put(10, new HashMap<String, Integer>());
        citiesInBrasil.get(10).put("Belém", 109);
        citiesInBrasil.put(11, new HashMap<String, Integer>());
        citiesInBrasil.get(11).put("Apucarana", 912);
        citiesInBrasil.get(11).put("Cascavel", 115);
        citiesInBrasil.get(11).put("Curitiba", 116);
        citiesInBrasil.get(11).put("Maringá", 119);
        citiesInBrasil.get(11).put("Ponta Grossa", 1038);
        citiesInBrasil.put(12, new HashMap<String, Integer>());
        citiesInBrasil.get(12).put("Recife", 114);
        citiesInBrasil.put(13, new HashMap<String, Integer>());
        citiesInBrasil.get(13).put("Duque de Caxias", 122);
        citiesInBrasil.get(13).put("Niterói", 124);
        citiesInBrasil.get(13).put("Nova Iguaçu", 125);
        citiesInBrasil.get(13).put("Petrópolis", 126);
        citiesInBrasil.get(13).put("Rio de Janeiro - Barra da Tijuca", 1115);
        citiesInBrasil.get(13).put("Rio de Janeiro - Botafogo", 1105);
        citiesInBrasil.get(13).put("Rio de Janeiro - Campo Grande", 1107);
        citiesInBrasil.get(13).put("Rio de Janeiro - Catete", 1161);
        citiesInBrasil.get(13).put("Rio de Janeiro - Del Castiho", 127);
        citiesInBrasil.get(13).put("Rio de Janeiro - Flamengo", 1187);
        citiesInBrasil.get(13).put("Rio de Janeiro - Gávea", 1113);
        citiesInBrasil.get(13).put("Rio de Janeiro - Leblon", 1117);
        citiesInBrasil.get(13).put("Rio de Janeiro - Madureira", 1119);
        citiesInBrasil.get(13).put("Rio de Janeiro - Nilópolis", 1111);
        citiesInBrasil.get(13).put("Rio de Janeiro - Piedade", 1121);
        citiesInBrasil.get(13).put("Rio de Janeiro - São Conrado", 1123);
        citiesInBrasil.get(13).put("Rio de Janeiro - Tijuca", 1126);
        citiesInBrasil.get(13).put("São João do Meriti", 129);
        citiesInBrasil.get(13).put("Volta Redonda", 1042);
        citiesInBrasil.put(14, new HashMap<String, Integer>());
        citiesInBrasil.get(14).put("Natal", 130);
        citiesInBrasil.put(15, new HashMap<String, Integer>());
        citiesInBrasil.get(15).put("Bento Gonçalves", 131);
        citiesInBrasil.get(15).put("Cachoeirinha", 132);
        citiesInBrasil.get(15).put("Canoas", 133);
        citiesInBrasil.get(15).put("Caxias Do Sul", 915);
        citiesInBrasil.get(15).put("Lajeado", 1017);
        citiesInBrasil.get(15).put("Passo Fundo", 1014);
        citiesInBrasil.get(15).put("Pelotas", 135);
        citiesInBrasil.get(15).put("Porto Alegre", 136);
        citiesInBrasil.get(15).put("Santa Cruz do Sul", 900);
        citiesInBrasil.get(15).put("Santa Maria", 902);
        citiesInBrasil.put(16, new HashMap<String, Integer>());
        citiesInBrasil.get(16).put("Balneário Camboriú", 914);
        citiesInBrasil.get(16).put("Camboriú", 138);
        citiesInBrasil.get(16).put("Chapecó", 1019);
        citiesInBrasil.get(16).put("Criciúma", 139);
        citiesInBrasil.get(16).put("Florianopolis", 1143);
        citiesInBrasil.get(16).put("Itajaí", 141);
        citiesInBrasil.get(16).put("Jaraguá do Sul", 142);
        citiesInBrasil.get(16).put("Joinville", 143);
        citiesInBrasil.get(16).put("Lages", 44);
        citiesInBrasil.get(16).put("Palhoça", 140);
        citiesInBrasil.get(16).put("São José", 145);
        citiesInBrasil.get(16).put("Tubarão", 1045);
        citiesInBrasil.put(17, new HashMap<String, Integer>());
        citiesInBrasil.get(17).put("Araçatuba", 1036);
        citiesInBrasil.get(17).put("Barueri", 150);
        citiesInBrasil.get(17).put("Brotas", 1145);
        citiesInBrasil.get(17).put("Campinas", 152);
        citiesInBrasil.get(17).put("Cotia", 1149);
        citiesInBrasil.get(17).put("Diadema", 1028);
        citiesInBrasil.get(17).put("Guarulhos", 156);
        citiesInBrasil.get(17).put("Guarulhos - Parque Cecap", 971);
        citiesInBrasil.get(17).put("Guedala", 921);
        citiesInBrasil.get(17).put("Ipiranga", 1009);
        citiesInBrasil.get(17).put("Jacareí", 159);
        citiesInBrasil.get(17).put("Jardim América", 1147);
        citiesInBrasil.get(17).put("Limeira", 321);
        citiesInBrasil.get(17).put("Osasco", 165);
        citiesInBrasil.get(17).put("Osasco - Vila Yara", 946);
        citiesInBrasil.get(17).put("Piracicaba", 166);
        citiesInBrasil.get(17).put("Presidente Prudente", 168);
        citiesInBrasil.get(17).put("Ribeirão Preto", 169);
        citiesInBrasil.get(17).put("Rio Claro", 170);
        citiesInBrasil.get(17).put("S.Paulo - Alto de Pinheiros", 974);
        citiesInBrasil.get(17).put("S.Paulo - Barra Funda", 948);
        citiesInBrasil.get(17).put("S.Paulo - Bela Vista", 949);
        citiesInBrasil.get(17).put("S.Paulo - Bosque da Saúde", 954);
        citiesInBrasil.get(17).put("S.Paulo - Butantã", 41);
        citiesInBrasil.get(17).put("S.Paulo - Canindé", 956);
        citiesInBrasil.get(17).put("S.Paulo - Cidade Jardim", 957);
        citiesInBrasil.get(17).put("S.Paulo - Consolação", 959);
        citiesInBrasil.get(17).put("S.Paulo - Higienópolis", 1002);
        citiesInBrasil.get(17).put("S.Paulo - Itam Bibi", 980);
        citiesInBrasil.get(17).put("S.Paulo - Jardim das Acácias", 978);
        citiesInBrasil.get(17).put("S.Paulo - Jardim Paulista", 977);
        citiesInBrasil.get(17).put("S.Paulo - Jardim Umuarama", 975);
        citiesInBrasil.get(17).put("S.Paulo - Paraíso", 972);
        citiesInBrasil.get(17).put("S.Paulo - Perdizes", 645);
        citiesInBrasil.get(17).put("S.Paulo - Pinheiros", 968);
        citiesInBrasil.get(17).put("S.Paulo - Quinta da Paineira", 967);
        citiesInBrasil.get(17).put("S.Paulo - Tatuapé", 964);
        citiesInBrasil.get(17).put("S.Paulo - Vila Almeida", 938);
        citiesInBrasil.get(17).put("S.Paulo - Vila Cordeiro", 941);
        citiesInBrasil.get(17).put("S.Paulo - Vila Guilherme", 942);
        citiesInBrasil.get(17).put("S.Paulo - Vila Mariana", 943);
        citiesInBrasil.get(17).put("S.Paulo - Villa Lobo", 1167);
        citiesInBrasil.get(17).put("Santana", 922);
        citiesInBrasil.get(17).put("Santo André", 172);
        citiesInBrasil.get(17).put("Santos", 173);
        citiesInBrasil.get(17).put("São Bernardo do Campo", 174);
        citiesInBrasil.get(17).put("São José dos Campos", 176);
        citiesInBrasil.get(17).put("São Paulo", 901);
        citiesInBrasil.get(17).put("Säo Paulo - Pinheiros", 1023);
        citiesInBrasil.put(18, new HashMap<String, Integer>());
        citiesInBrasil.get(18).put("Aracaju", 146);
        states = new HashMap<String, Integer>();
        states.put("Todos Estados", 0);
        states.put("Alagoas", 1);
        states.put("Amazonas", 2);
        states.put("Bahia", 3);
        states.put("Ceará", 4);
        states.put("Distrito Federal", 5);
        states.put("Espírito Santo", 6);
        states.put("Goiás", 7);
        states.put("Mato Grosso do Sul", 8);
        states.put("Minas Gerais", 9);
        states.put("Pará", 10);
        states.put("Paraná", 11);
        states.put("Pernambuco", 12);
        states.put("Rio de Janeiro", 13);
        states.put("Rio Grande do Norte", 14);
        states.put("Rio Grande do Sul", 15);
        states.put("Santa Catarina", 16);
        states.put("São Paulo", 17);
        states.put("Sergipe", 18);
        genres = new HashMap<String, String>();
        genres.put("Todos", "http://www.cinemaki.com.br/shows");
        genres.put("Animação", "http://www.cinemaki.com.br/Anima%C3%A7%C3%A3o-/shows/tag/54");
        genres.put("Aventura", "http://www.cinemaki.com.br/Aventura-/shows/tag/53");
        genres.put("Ação", "http://www.cinemaki.com.br/A%C3%A7%C3%A3o-/shows/tag/52");
        genres.put("Biografia", "http://www.cinemaki.com.br/Biografia-/shows/tag/55");
        genres.put("Comédia", "http://www.cinemaki.com.br/Com%C3%A9dia-/shows/tag/56");
        genres.put("Documentário", "http://www.cinemaki.com.br/Document%C3%A1rio-/shows/tag/58");
        genres.put("Drama", "http://www.cinemaki.com.br/Drama-/shows/tag/59");
        genres.put("Familiar", "http://www.cinemaki.com.br/Familiar-/shows/tag/60");
        genres.put("Fantasia", "http://www.cinemaki.com.br/Fantasia-/shows/tag/61");
        genres.put("Ficção", "http://www.cinemaki.com.br/Fic%C3%A7%C3%A3o%20Cient%C3%ADfica-/shows/tag/72");
        genres.put("Mistério", "http://www.cinemaki.com.br/Mist%C3%A9rio-/shows/tag/68");
        genres.put("Musical", "http://www.cinemaki.com.br/Musical-/shows/tag/67");
        genres.put("Policial", "http://www.cinemaki.com.br/Policial-/shows/tag/57");
        genres.put("Romance", "http://www.cinemaki.com.br/Romance-/shows/tag/71");
        genres.put("Suspense", "http://www.cinemaki.com.br/Suspense-/shows/tag/76");
        genres.put("Terror", "http://www.cinemaki.com.br/Terror-/shows/tag/65");
    }

    public String[] getAllCities() {
        ArrayList<String> cities = new ArrayList<String>();
        for (Integer key : citiesInBrasil.keySet()) {
            cities.addAll(citiesInBrasil.get(key).keySet());
        }
        String[] arrayCities = new String[cities.size()];
        for (int i = 0; i < cities.size(); i++) {
            arrayCities[i] = cities.get(i);
        }
        return arrayCities;
    }

    public String[] getCitiesFrom(Integer stateCode) {
        ArrayList<String> cities = new ArrayList<String>();
        if (citiesInBrasil.containsKey(stateCode)) {
            cities.addAll(citiesInBrasil.get(stateCode).keySet());
        }
        String[] arrayCities = new String[cities.size()];
        for (int i = 0; i < cities.size(); i++) {
            arrayCities[i] = cities.get(i);
        }
        return arrayCities;
    }

    public Integer getCityCode(String cityName) {
        for (Integer i : citiesInBrasil.keySet()) {
            if (citiesInBrasil.get(i).containsKey(cityName)) {
                return citiesInBrasil.get(i).get(cityName);
            }
        }
        return null;
    }

    public Integer getStateCode(String StateName) {
        if (states.containsKey(StateName)) {
            return states.get(StateName);
        } else return null;
    }

    public String[] getAllStates() {
        Object[] statesSet = states.keySet().toArray();
        String[] statesArray = new String[statesSet.length];
        for (int i = 0; i < statesArray.length; i++) {
            statesArray[i] = (String) statesSet[i];
        }
        return statesArray;
    }

    public String getGenreUrl(String genreName) {
        if (genres.containsKey(genreName)) {
            return genres.get(genreName);
        } else return null;
    }

    public String[] getAllGenres() {
        Object[] genresSet = genres.keySet().toArray();
        String[] genresArray = new String[genresSet.length];
        for (int i = 0; i < genresArray.length; i++) {
            genresArray[i] = (String) genresSet[i];
        }
        return genresArray;
    }
}
