package org.pwr.odwa.common.metadata;

import java.util.ArrayList;

/** Klasa reprezentuje tabelę wymiarów.
 *
 *  Tabelą wymiarów nazywamy tabelę relacyjnej bazy danych,
 *  zawierającą atrybuty opisowe, najczęściej zgrupowane
 *  w poziomy (w przypadku architektury w postaci gwiazdy).
 *  Nadaje znaczenie elementom atrybutom tabeli faktów
 *  (definiuje przestrzeń faktów). Tabela wymiarów zawiera
 *  dane rzadko podlegające zmianom.
 *
 *  Przykład:
 *
 *     +---------+
 *     | Czas    |
 *     +---------+
 *     | Rok     |
 *     | Miesiąc |
 *     | Tydzień |
 *     | Dzień   |
 *     +---------+
 *
 *  Tabela wymiarów Czas zawiera cztery atrybuty, z których
 *  każdy definiuje wymiar czasu, dodatkowo tworząc poziomy.
 *
 *  @author Mateusz Paprocki
 *  @author Maria Łabaziewicz
 */
public class MetaDimTable extends MetaElement {

    private ArrayList<MetaDim> m_dims;

    protected void setDimensions(ArrayList<MetaDim> dims) {
        m_dims = dims;
    }

    public ArrayList<MetaDim> getDimensions() {
        return m_dims;
    }
}
