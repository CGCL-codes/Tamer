package br.padroes.gof.criacao.abstractfactory;

class MotifWidgetFactory extends WidgetFactory {

    public Botao criarBotao() {
        return new BotaoMotif();
    }
}
