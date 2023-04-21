package Auxiliar;

import Simulador.Event.Evento;

/**
 *
 * @author A
 */
public class FilaEventos extends Fila {

    public FilaEventos() {
        super();
    }

    @Override
    public void addItem(Object Obj) {
        if (Obj == null) {
            System.out.println("Erro - addItem() - Obj = null");
        }
        if (!(Obj instanceof Evento)) {
            System.out.println("Erro - addItem() - FilaEventos");
            return;
        }
        float tempoNovo = ((Evento) Obj).getMomentoDeChegada();
        Item itemNovo = new Item(Obj);
        Item anterior = head, posterior = head;
        if (head == null) {
            head = itemNovo;
            tail = itemNovo;
        } else {
            while (((Evento) posterior.getObject()).getMomentoDeChegada() <= tempoNovo && posterior.next != null) {
                anterior = posterior;
                posterior = posterior.next;
            }
            if (posterior.next == null && ((Evento) posterior.getObject()).getMomentoDeChegada() <= tempoNovo) {
                posterior.next = itemNovo;
                tail = itemNovo;
                return;
            }
            if (anterior == head && ((Evento) anterior.getObject()).getMomentoDeChegada() >= tempoNovo) {
                itemNovo.setNext(head);
                head = itemNovo;
            } else {
                anterior.setNext(itemNovo);
                itemNovo.setNext(posterior);
            }
        }
    }
}
