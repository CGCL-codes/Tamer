package edu.udo.scaffoldhunter.view.table;

import java.util.List;

/**
 * @author Micha
 *
 */
public class SvgLoadingTrigger extends Thread {

    private ViewComponent viewComponent;

    private Model model;

    private long updatetime;

    private long delay = 200;

    private boolean threadIsRunning;

    /**
     * @param viewComponent 
     * @param model 
     * 
     */
    public SvgLoadingTrigger(ViewComponent viewComponent, Model model) {
        super();
        this.viewComponent = viewComponent;
        this.model = model;
        updatetime = Long.MAX_VALUE;
    }

    /**
     * 
     */
    @Override
    public void run() {
        threadIsRunning = true;
        while (threadIsRunning) {
            if (System.currentTimeMillis() > updatetime) {
                updatetime = Long.MAX_VALUE;
                List<Integer> rowList = viewComponent.getCurrentlyVisibleModelRows();
                for (Integer row : rowList) {
                    model.loadAnSvg(row);
                }
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * call this method when the table is repainted. SVGs will be loaded
     * 200ms after the last call to update().
     */
    public void update() {
        updatetime = System.currentTimeMillis() + delay;
    }

    /**
     * ends the thread
     */
    public void shutdown() {
        threadIsRunning = false;
    }
}
