package antirashka.map.generation;

final class Cross {

    final Segment cross;

    final boolean barricade;

    Cross(Segment cross, boolean barricade) {
        this.cross = cross;
        this.barricade = barricade;
    }
}
