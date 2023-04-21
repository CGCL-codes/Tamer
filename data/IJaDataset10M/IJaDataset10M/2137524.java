/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tutorial.DesignPatterns.Observer.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Clasa Observabila, cea care publica evenimente la Listenerii sai.
 * si momentan nu stie cine is Listeneri si nici la ce evenimente raspund
 * 
 * @author Boogie
 */
public class Caine {
    private List<CaineListener> listeners = new ArrayList<>();
    private String name;

    public Caine(String name) {
        this.name = name;
    }
    /**
     * adauga Listener
     * @param listener
     */
    public void addStapan(CaineListener listener){
        listeners.add(listener);
    }
    /**
     * scoate Listener
     * @param listener 
     */
    public void removeStapan(CaineListener listener){
        listeners.remove(listener);
    }
    /**
     * publica eveniment HUNGRY 
     * 
     */
    public void isHungry(){
        for(CaineListener l : listeners){
            /* asta ii un for mai pizdos 
             * practic ii la fel cu
             *
             * for(int i=0;i<listeners.size();i++{
             *      CaineListener l = listeners.get(i);
             * ...
             * 
             * face acelasi lucru dar scrii mai putzin :D
             * ii aia ce o zis Robu
             * Enchanced For Loop
             */
            l.onCaineEvent(new CaineEvent(CaineEvent.HUNGRY,this));
        }
    }
    /**
     * publica eveniment SLEEPY
     * 
     */
    public void isSleepy(){
       for(CaineListener l : listeners){
            l.onCaineEvent(new CaineEvent(CaineEvent.SLEEPY,this));
        } 
    }
    /**
     * cand in printezi sa scrie variabila nume     * 
     * @return numele cainelui in cauza
     */
    @Override
    public String toString() {
        return this.name;
    }
    
}
