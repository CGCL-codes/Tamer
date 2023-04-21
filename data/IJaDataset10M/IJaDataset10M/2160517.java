package cin.aquario;

import org.simulare.*;

public class SimulationAquario {

    public static Simulation sim;

    public static VisualTask view = new Report3D();

    static {
        sim = new Simulation();
        sim.addItem(Example.aquario);
        sim.addItem(Example.coral);
        sim.addItem(Example.presa);
        sim.addItem(Example.predador);
        Task breath = new ActionBreath();
        Task move = new ActionMove();
        Task print = new ReportPrintln();
        sim.addTask(breath);
        sim.addTask(move);
        sim.addTask(print);
        sim.addTask(view);
        ItemListener listener = new NoOxigen();
        Example.aquario.addItemListener(listener);
        Task addItem = new AddItem(Example.aquario);
        sim.addSchedule(new ScheduledTask(addItem, Example.coral, 0));
        sim.addSchedule(new ScheduledTask(addItem, Example.presa, 0));
        sim.addSchedule(new ScheduledTask(addItem, Example.predador, 0));
        Task setAlive = new SetupBoolean(LivingThing.IS_ALIVE, true);
        sim.addSchedule(new ScheduledTask(setAlive, Example.coral, 0));
        sim.addSchedule(new ScheduledTask(setAlive, Example.presa, 0));
        sim.addSchedule(new ScheduledTask(setAlive, Example.predador, 0));
        Task setupAir = new SetupRandom(ActionBreath.PRODUCER, 1000, 1500);
        sim.addSchedule(new ScheduledTask(setupAir, Example.aquario, 0));
        Task consumeAirRate = new SetupRandom(ActionBreath.CONSUMER, 0, 10);
        sim.addSchedule(new ScheduledTask(consumeAirRate, Example.coral, 0));
        sim.addSchedule(new ScheduledTask(consumeAirRate, Example.presa, 0));
        sim.addSchedule(new ScheduledTask(consumeAirRate, Example.predador, 0));
        sim.addSchedule(new PeriodicTask(breath, Example.coral, 1, 1));
        sim.addSchedule(new PeriodicTask(breath, Example.presa, 2, 2));
        sim.addSchedule(new PeriodicTask(breath, Example.predador, 3, 3));
        sim.addSchedule(new PeriodicTask(print, Example.aquario, 1, 1));
        double altura = Example.aquario.getAttribute("altura").getDouble();
        double largura = Example.aquario.getAttribute("largura").getDouble();
        double comprimento = Example.aquario.getAttribute("comprimento").getDouble();
        Task setupX = new SetupRandom(ActionMove.CONSUMERX, 0, altura);
        sim.addSchedule(new ScheduledTask(setupX, Example.coral, 0));
        sim.addSchedule(new ScheduledTask(setupX, Example.presa, 0));
        sim.addSchedule(new ScheduledTask(setupX, Example.predador, 0));
        Task setupY = new SetupRandom(ActionMove.CONSUMERY, 0, largura);
        sim.addSchedule(new ScheduledTask(setupY, Example.presa, 0));
        sim.addSchedule(new ScheduledTask(setupY, Example.predador, 0));
        Task setupZ = new SetupRandom(ActionMove.CONSUMERZ, 0, comprimento);
        sim.addSchedule(new ScheduledTask(setupZ, Example.coral, 0));
        sim.addSchedule(new ScheduledTask(setupZ, Example.presa, 0));
        sim.addSchedule(new ScheduledTask(setupZ, Example.predador, 0));
        sim.addSchedule(new PeriodicTask(move, Example.coral, 1, 3));
        sim.addSchedule(new PeriodicTask(move, Example.presa, 1, 2));
        sim.addSchedule(new PeriodicTask(move, Example.predador, 1, 1));
        sim.addSchedule(new PeriodicTask(view, Example.aquario, 1, 1));
        sim.addSchedule(new PeriodicTask(view, Example.coral, 1, 1));
        sim.addSchedule(new PeriodicTask(view, Example.presa, 1, 1));
        sim.addSchedule(new PeriodicTask(view, Example.predador, 1, 1));
        ActionEat eat = new ActionEat("fish_eats_coral");
        eat.setFood(Example.coral.getName());
        eat.setRadius(1);
        sim.addSchedule(new PeriodicTask(eat, Example.presa, 1, 1));
        eat = new ActionEat("predator_eats_coral");
        eat.setFood(Example.coral.getName());
        eat.setRadius(1.5);
        sim.addSchedule(new PeriodicTask(eat, Example.predador, 1, 1));
        eat = new ActionEat("predator_eats_fish");
        eat.setFood(Example.presa.getName());
        eat.setRadius(2);
        sim.addSchedule(new PeriodicTask(eat, Example.predador, 1, 1));
    }
}
